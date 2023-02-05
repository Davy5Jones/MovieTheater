package com.jb.MovieTheater.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@PropertySource("classpath:admin.properties")
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class SecurityConfig {
    private final CustomerUserService customerUserService;
    private final ClerkUserService clerkUserService;

    private RSAKey rsaKey;

    @Value("${string.password}")
    private String password;
    @Value("${string.email}")
    private String email;


    @Bean
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        return http.cors().and().csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .mvcMatchers("home/**").permitAll()
                        .mvcMatchers("api/clerk/**").hasAuthority("SCOPE_ROLE_CLERK")
                        .mvcMatchers("api/customer/**").hasAnyAuthority("SCOPE_ROLE_CUSTOMER", "SCOPE_ROLE_CLERK")
                        .mvcMatchers("api/admin/**").hasAuthority("SCOPE_ROLE_ADMIN")
                        .anyRequest().authenticated()
                )
                // This enables our JWT Authentication
                .oauth2ResourceServer(oath->{
                    oath.bearerTokenResolver(request -> {
                        try {
                            return attemptAuthentication(request);
                        } catch (JOSEException e) {
                            throw new RuntimeException(e);
                        }
                    }).jwt(jwtConfigurer -> {
                        try {
                            jwtDecoder();
                        } catch (JOSEException e) {
                            throw new RuntimeException(e);
                        }
                    });
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    public String attemptAuthentication(HttpServletRequest request) throws AuthenticationException, JOSEException {
        // get token from a Cookie
        if (request.getRequestURI().contains("home")){
            return null;
        }
        Cookie[] cookies = request.getCookies();


        if( cookies == null || cookies.length < 1 ) {
            return null;
        }
        System.out.println(cookies.length);
        Cookie sessionCookie = null;
        for( Cookie cookie : cookies ) {
            if( ( "movieSessionId" ).equals( cookie.getName() ) ) {
                sessionCookie = cookie;
                break;
            }
        }

        // TODO: move the cookie validation into a private method
        if( sessionCookie == null || StringUtils.isEmpty( sessionCookie.getValue() ) ) {
            return null;
        }
        return sessionCookie.getValue();
    }

    @Bean
    @Qualifier
    public AuthenticationProvider customerAuthenticationProvider() {
        final DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        provider.setUserDetailsService(customerUserService);
        return provider;
    }

    @Bean
    @Qualifier
    public AuthenticationProvider adminAuthenticationProvider() {
        return new AuthenticationProvider() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                String passw = authentication.getCredentials().toString();
                String ema = authentication.getName();
                if (!Objects.equals(passw, password) || !Objects.equals(ema, email)) {
                    throw  new UsernameNotFoundException("Username not found: " + email);
                }
                List<GrantedAuthority> list = new ArrayList<>();
                list.add(new SimpleGrantedAuthority(("ROLE_ADMIN")));
                return new UsernamePasswordAuthenticationToken(ema, passw, list);
            }

            @Override
            public boolean supports(Class<?> authentication) {
                return authentication.equals(UsernamePasswordAuthenticationToken.class);
            }
        };
    }

    @Bean
    @Qualifier
    public AuthenticationProvider clerkAuthenticationProvider() {
        final DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        provider.setUserDetailsService(clerkUserService);
        return provider;
    }


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }


    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        rsaKey = Jwks.generateRsa();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwks) {
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public JwtDecoder jwtDecoder() throws JOSEException {
        return NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey()).build();
    }


    @Bean
    public WebMvcConfigurer corsConfigurer() {

        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                        .addMapping("/**")
                        .allowedMethods(CorsConfiguration.ALL)
                        .allowedHeaders(CorsConfiguration.ALL)
                        .allowCredentials(true)
                        .allowedOriginPatterns(CorsConfiguration.ALL);
            }
        };
    }
}


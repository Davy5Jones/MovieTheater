package com.jb.MovieTheater.security;

import com.jb.MovieTheater.beans.mongo.Movie;
import com.jb.MovieTheater.models.screening.ScreeningModelDto;
import com.jb.MovieTheater.models.user.CustomerModelDao;
import com.jb.MovieTheater.services.HomeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("home")
public class HomeController {
    private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);
    private final AuthenticationManager clerkAuthenticationManager;
    private final AuthenticationManager customerAuthenticationManager;
    private final AuthenticationManager adminAuthenticationManager;
    private final TokenService tokenService;
    private final HomeService homeService;

    public HomeController(@Qualifier AuthenticationProvider clerkAuthenticationProvider, @Qualifier AuthenticationProvider customerAuthenticationProvider, @Qualifier AuthenticationProvider adminAuthenticationProvider, TokenService tokenService, HomeService homeService) {
        this.clerkAuthenticationManager = new ProviderManager(clerkAuthenticationProvider);
        this.customerAuthenticationManager = new ProviderManager(customerAuthenticationProvider);
        this.adminAuthenticationManager = new ProviderManager(adminAuthenticationProvider);
        this.tokenService = tokenService;
        this.homeService = homeService;
    }

    @PostMapping("login/customer")
    public String customerLogin(@RequestBody LoginBodyModel userLogin) {
        LOG.debug("Login requested for customer: '{}'", userLogin.getEmail());
        Authentication authentication = customerAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLogin.getEmail(), userLogin.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER"))));
        return tokenService.generateToken(authentication, userLogin.isStayLoggedIn());
    }

    @PostMapping("login/company")
    public String companyLogin(@RequestBody LoginBodyModel userLogin) {
        LOG.debug("Login requested for company: '{}'", userLogin.getEmail());
        Authentication authentication = clerkAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLogin.getEmail(), userLogin.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_COMPANY"))));
        return tokenService.generateToken(authentication, userLogin.isStayLoggedIn());
    }

    @PostMapping("login/admin")
    public String adminLogin(@RequestBody LoginBodyModel userLogin) {
        LOG.debug("Login requested for admin: '{}'", userLogin.getEmail());
        Authentication authentication = adminAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLogin.getEmail(), userLogin.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))));
        return tokenService.generateToken(authentication, userLogin.isStayLoggedIn());
    }

    @PostMapping("register")
    public String customerRegister(@RequestBody CustomerModelDao user) {
        LOG.debug("Login requested for customer: '{}'", user.getEmailAddress());
        Authentication authentication = customerAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmailAddress(), user.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER"))));
        return tokenService.generateToken(authentication,false);
    }

    @GetMapping("screenings/today")
    public List<ScreeningModelDto> todayScreenings(@RequestParam int page){
        return homeService.todayScreeningsByTime(page,10);
    }

    @GetMapping("movies/recommended")
    List<Movie> getRecommendedMovies(){
        return homeService.getRecommendedMovies();
    }

}

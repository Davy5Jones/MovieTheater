package com.jb.MovieTheater.controller;

import com.jb.MovieTheater.assemblers.CustomerModelAssembler;
import com.jb.MovieTheater.exception.CustomCinemaException;
import com.jb.MovieTheater.models.user.CustomerModelDao;
import com.jb.MovieTheater.models.user.CustomerModelDto;
import com.jb.MovieTheater.security.LoginBodyModel;
import com.jb.MovieTheater.security.StaffLoginModel;
import com.jb.MovieTheater.security.TokenService;
import com.jb.MovieTheater.services.CustomerService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.executable.ValidateOnExecution;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("home")
public class HomeController {
    private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);
    private final AuthenticationManager clerkAuthenticationManager;
    private final AuthenticationManager customerAuthenticationManager;
    private final AuthenticationManager adminAuthenticationManager;
    private final CustomerService customerService;
    private final TokenService tokenService;
    private final HomeService homeService;
    private final CustomerModelAssembler customerModelAssembler;

    public HomeController(@Qualifier AuthenticationProvider clerkAuthenticationProvider, @Qualifier AuthenticationProvider customerAuthenticationProvider, @Qualifier AuthenticationProvider adminAuthenticationProvider, CustomerService customerService, TokenService tokenService, HomeService homeService, CustomerModelAssembler customerModelAssembler) {
        this.clerkAuthenticationManager = new ProviderManager(clerkAuthenticationProvider);
        this.customerAuthenticationManager = new ProviderManager(customerAuthenticationProvider);
        this.adminAuthenticationManager = new ProviderManager(adminAuthenticationProvider);
        this.customerService = customerService;
        this.tokenService = tokenService;
        this.homeService = homeService;
        this.customerModelAssembler = customerModelAssembler;
    }

    @PostMapping("login/customer")
    public CustomerModelDto customerLogin(@RequestBody @Validated LoginBodyModel userLogin, HttpServletResponse response) throws CustomCinemaException {
        LOG.debug("Login requested for customer: '{}'", userLogin.getEmail());
        Authentication authentication = customerAuthenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(userLogin.getEmail(),
                        userLogin.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER"))));

        String token = tokenService.generateToken(authentication, userLogin.isStayLoggedIn());
        Cookie cookie=new Cookie("movieSessionId",token);
        cookie.setHttpOnly(true);
        cookie.setPath("/api/customer");
        if (userLogin.isStayLoggedIn()) cookie.setMaxAge(10 * 365 * 24 * 60 * 6);
        response.addCookie(cookie);
        CustomerModelDto customerModelDto = customerModelAssembler.toModel(customerService.getCustomerDetails(Integer.parseInt(authentication.getName())));

        customerModelDto.add(linkTo(methodOn(CustomerController.class).getCustomerDetails(authentication)).withSelfRel());
        customerModelDto.add(linkTo(methodOn(CustomerController.class).findAllUserTickets(authentication, 0, 5)).withRel("tickets"));
        return customerModelDto;
    }

    @PostMapping("login/clerk")
    public String clerkLogin(@RequestBody @Validated StaffLoginModel userLogin) {
        LOG.debug("Login requested for CLERK: '{}'", userLogin.getEmail());
        Authentication authentication = clerkAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLogin.getEmail(), userLogin.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_CLERK"))));
        return tokenService.generateToken(authentication, false);
    }

    @PostMapping("login/admin")
    public void adminLogin(@RequestBody @Validated StaffLoginModel staffLoginModel, HttpServletResponse response) {
        LOG.debug("Login requested for admin: '{}'", staffLoginModel.getEmail());
        Authentication authentication = adminAuthenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(staffLoginModel.getEmail(), staffLoginModel.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))));
        Cookie cookie= new Cookie("movieSessionId",tokenService.generateToken(authentication, false));
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    @PostMapping("register")
    public void customerRegister(@RequestBody @Validated CustomerModelDao user) throws CustomCinemaException {
        LOG.debug("Register requested for customer: '{}'", user.getEmailAddress());
        homeService.register(user);
    }
}

package com.jb.MovieTheater.controller;

import com.jb.MovieTheater.exception.CustomCinemaException;
import com.jb.MovieTheater.models.ticket.TicketModelDao;
import com.jb.MovieTheater.models.ticket.TicketModelDto;
import com.jb.MovieTheater.models.user.CustomerModelDto;
import com.jb.MovieTheater.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/customer")
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("purchases")
    public TicketModelDto purchaseTicket(@RequestBody TicketModelDao ticket, Authentication authentication) throws CustomCinemaException {
        return customerService.purchaseTicket(ticket, Integer.parseInt(authentication.getName()));
    }

    @GetMapping("purchases")
    public List<TicketModelDto> findAllUserTickets(Authentication authentication) {
        return customerService.findAllUserTickets(Integer.parseInt(authentication.getName()));
    }

    @GetMapping
    public CustomerModelDto getCustomerDetails(Authentication authentication) throws CustomCinemaException {
        return customerService.getCustomerDetails(Integer.parseInt(authentication.getName()));
    }


}

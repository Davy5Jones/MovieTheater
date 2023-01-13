package com.jb.MovieTheater.controller;

import com.jb.MovieTheater.beans.mongo.Movie;
import com.jb.MovieTheater.exception.CustomCinemaException;
import com.jb.MovieTheater.models.ticket.TicketModelDao;
import com.jb.MovieTheater.models.ticket.TicketModelDto;
import com.jb.MovieTheater.models.user.CustomerModelDto;
import com.jb.MovieTheater.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/customer")
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("purchases")
    public TicketModelDto purchaseTicket(@RequestBody TicketModelDao ticket, Authentication authentication) throws CustomCinemaException {
        TicketModelDto ticketModelDto = customerService.purchaseTicket(ticket, Integer.parseInt(authentication.getName()));
        ticketModelDto.add(linkTo(methodOn(CustomerController.class).findSingleUserTicket(authentication, ticketModelDto.getId())).withSelfRel());
        return ticketModelDto;
    }

    @GetMapping("purchases/{ticketId}")
    public TicketModelDto findSingleUserTicket(Authentication authentication, @PathVariable String ticketId) throws CustomCinemaException {
        TicketModelDto singleUserTicket = customerService.findSingleUserTicket(ticketId, Integer.parseInt(authentication.getName()));
        singleUserTicket.add(linkTo(methodOn(CustomerController.class).findSingleUserTicket(authentication, ticketId)).withSelfRel());
        return singleUserTicket;
    }

    @GetMapping("purchases")
    public List<TicketModelDto> findAllUserTickets(Authentication authentication) throws CustomCinemaException {
        List<TicketModelDto> allUserTickets = customerService.findAllUserTickets(Integer.parseInt(authentication.getName()));
        for (TicketModelDto ticket : allUserTickets) {
            ticket.add(linkTo(methodOn(CustomerController.class).findSingleUserTicket(authentication, ticket.getId())).withSelfRel());
        }
        return allUserTickets;
    }

    @GetMapping
    public CustomerModelDto getCustomerDetails(Authentication authentication) throws CustomCinemaException {
        CustomerModelDto customerDetails = customerService.getCustomerDetails(Integer.parseInt(authentication.getName()));
        for (TicketModelDto ticket : customerDetails.getCustomerTickets()) {
            ticket.add(linkTo(methodOn(CustomerController.class).findSingleUserTicket(authentication, ticket.getId())).withSelfRel());
        }
        return customerDetails;
    }

    @GetMapping("movies/recommended")
    List<Movie> getRecommendedMovies() {
        return customerService.getRecommendedMovies();
    }


}

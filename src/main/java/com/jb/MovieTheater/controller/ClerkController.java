package com.jb.MovieTheater.controller;

import com.jb.MovieTheater.assemblers.ClerkModelAssembler;
import com.jb.MovieTheater.assemblers.TicketModelAssembler;
import com.jb.MovieTheater.beans.mongo.Purchase;
import com.jb.MovieTheater.exception.CustomCinemaException;
import com.jb.MovieTheater.models.ticket.TicketModelDto;
import com.jb.MovieTheater.models.user.ClerkModelDto;
import com.jb.MovieTheater.services.ClerkService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/clerk")
public class ClerkController {

    private final ClerkService clerkService;
    private final String defaultPageSize = "5";
    private final TicketModelAssembler ticketModelAssembler;
    private final PagedResourcesAssembler<Purchase> pagedPurchaseResourcesAssembler;
    private final ClerkModelAssembler clerkModelAssembler;



    @PutMapping("purchases/{purchaseId}")
    public TicketModelDto invalidatePurchase(@PathVariable String purchaseId) throws CustomCinemaException {
        TicketModelDto ticketModelDto = ticketModelAssembler.toModel(clerkService.invalidatePurchase(purchaseId));
        ticketModelDto.add(linkTo(methodOn(ClerkController.class).findSingleUserTicket(purchaseId)).withSelfRel());
        return ticketModelDto;
    }

    @GetMapping("purchases/{purchaseId}")
    public TicketModelDto findSingleUserTicket(@PathVariable String purchaseId) throws CustomCinemaException {
        TicketModelDto ticket = ticketModelAssembler.toModel(clerkService.findSingleUserTicket(purchaseId));
        ticket.add(linkTo(methodOn(ClerkController.class).findSingleUserTicket(purchaseId)).withSelfRel());
        return ticket;
    }

    @GetMapping("purchases/by/customer")
    public PagedModel<TicketModelDto> findCustomerTicketsPageByEmail(@RequestParam String email, @RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = defaultPageSize) int pageSize, @RequestParam(required = false, defaultValue = "purchaseTime") String sortBy) throws CustomCinemaException {
        Page<Purchase> purchasePage = clerkService.findCustomerTicketsPageByEmail(email, page, pageSize, sortBy);
        PagedModel<TicketModelDto> pagedModel = pagedPurchaseResourcesAssembler.toModel(purchasePage, ticketModelAssembler, linkTo(methodOn(ClerkController.class).findCustomerTicketsPageByEmail(email, page, pageSize, sortBy)).withSelfRel());
        for (TicketModelDto ticket : pagedModel) {
            ticket.add(linkTo(methodOn(ClerkController.class).findSingleUserTicket(ticket.getId())).withSelfRel());
        }
        return pagedModel;
    }
    @GetMapping
    public ClerkModelDto getClerkDetails(Authentication authentication) throws CustomCinemaException{
        return clerkModelAssembler.toModel(clerkService.getClerkDetails(Integer.parseInt(authentication.getName())));
    }

}

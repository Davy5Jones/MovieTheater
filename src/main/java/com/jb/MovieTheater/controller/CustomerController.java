package com.jb.MovieTheater.controller;

import com.jb.MovieTheater.assemblers.CustomerModelAssembler;
import com.jb.MovieTheater.assemblers.MovieModelAssembler;
import com.jb.MovieTheater.assemblers.ScreeningModelAssembler;
import com.jb.MovieTheater.assemblers.TicketModelAssembler;
import com.jb.MovieTheater.beans.mongo.Movie;
import com.jb.MovieTheater.beans.mongo.Purchase;
import com.jb.MovieTheater.beans.mongo.Screening;
import com.jb.MovieTheater.exception.CustomCinemaException;
import com.jb.MovieTheater.models.movie.MovieModelDto;
import com.jb.MovieTheater.models.screening.ScreeningModelDto;
import com.jb.MovieTheater.models.ticket.TicketModelDao;
import com.jb.MovieTheater.models.ticket.TicketModelDto;
import com.jb.MovieTheater.models.user.CustomerModelDto;
import com.jb.MovieTheater.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/customer")
public class CustomerController {
    private final CustomerService customerService;
    private final PagedResourcesAssembler<Purchase> pagedPurchaseResourcesAssembler;

    private final PagedResourcesAssembler<Movie> pagedMovieResourcesAssembler;
    private final PagedResourcesAssembler<Screening> pagedScreeningMovieResourcesAssembler;

    private final TicketModelAssembler ticketModelAssembler;
    private final CustomerModelAssembler customerModelAssembler;
    private final MovieModelAssembler movieModelAssembler;
    private final ScreeningModelAssembler screeningModelAssembler;


    @PostMapping("purchases")
    @ResponseStatus(HttpStatus.CREATED)
    public TicketModelDto purchaseTicket(@RequestBody TicketModelDao ticket, Authentication authentication) throws CustomCinemaException {
        TicketModelDto ticketModelDto = ticketModelAssembler.toModel(customerService.purchaseTicket(ticket, Integer.parseInt(authentication.getName())));
        ticketModelDto.add(linkTo(methodOn(CustomerController.class).findSingleUserTicket(authentication, ticketModelDto.getId())).withSelfRel());
        return ticketModelDto;
    }

    @GetMapping("purchases")
    public PagedModel<TicketModelDto> findAllUserTickets(Authentication authentication, @RequestParam(defaultValue = "0", required = false) int page, @RequestParam(defaultValue = "5", required = false) int pageSize) throws CustomCinemaException {
        Page<Purchase> ticketsPageable = customerService.findAllUserTickets(page, pageSize, Integer.parseInt(authentication.getName()));
        PagedModel<TicketModelDto> pagedModel = pagedPurchaseResourcesAssembler.toModel(ticketsPageable, ticketModelAssembler);
        for (TicketModelDto ticketModelDto : pagedModel) {
            ticketModelDto.add(linkTo(methodOn(CustomerController.class).findSingleUserTicket(authentication, ticketModelDto.getId())).withSelfRel());
        }
        return pagedModel;
    }

    @GetMapping("purchases/{ticketId}")
    public TicketModelDto findSingleUserTicket(Authentication authentication, @PathVariable String ticketId) throws CustomCinemaException {
        TicketModelDto singleUserTicket = ticketModelAssembler.toModel(customerService.findSingleUserTicket(ticketId, Integer.parseInt(authentication.getName())));
        singleUserTicket.add(linkTo(methodOn(CustomerController.class).findSingleUserTicket(authentication, ticketId)).withSelfRel());
        return singleUserTicket;
    }


    @GetMapping
    public CustomerModelDto getCustomerDetails(Authentication authentication) throws CustomCinemaException {
        CustomerModelDto customerDetails = customerModelAssembler.toModel(customerService.getCustomerDetails(Integer.parseInt(authentication.getName())));
        customerDetails.add(linkTo(methodOn(CustomerController.class).getCustomerDetails(authentication)).withSelfRel());
        customerDetails.add(linkTo(methodOn(CustomerController.class).findAllUserTickets(authentication,0,5)).withRel("tickets"));
        return customerDetails;
    }

    @GetMapping("movies/recommended")
    public List<MovieModelDto> getRecommendedMovies() throws CustomCinemaException {
        List<MovieModelDto> recommendedMovies = customerService.getRecommendedMovies().stream().map(movieModelAssembler::toModel).collect(Collectors.toList());
        for (MovieModelDto movie : recommendedMovies) {
            movie.add(linkTo(methodOn(CustomerController.class).getSingleMovie(movie.getId())).withSelfRel());
            movie.add(linkTo(methodOn(CustomerController.class).getActiveScreeningsPageByMovie(0, 20, movie.getName(), "")).withRel("screenings"));
        }
        return recommendedMovies;
    }

    @GetMapping("movies/{movieId}")
    public MovieModelDto getSingleMovie(@PathVariable String movieId) throws CustomCinemaException {
        MovieModelDto movie = movieModelAssembler.toModel(customerService.getSingleMovie(movieId));
        movie.add(linkTo(methodOn(CustomerController.class).getSingleMovie(movie.getId())).withSelfRel());
        movie.add(linkTo(methodOn(CustomerController.class).getActiveScreeningsPageByMovie(0, 20, movie.getName(), "")).withRel("screenings"));
        return movie;
    }

    @GetMapping("screenings")
    public PagedModel<ScreeningModelDto> getActiveScreeningsPage(@RequestParam(defaultValue = "0", required = false) int page, @RequestParam(defaultValue = "5", required = false) int size, @RequestParam(defaultValue = "screenTime", required = false) String sort) throws CustomCinemaException {
        Page<Screening> screeningsPage = customerService.getActiveScreeningsPage(page, size, sort);
        PagedModel<ScreeningModelDto> pagedModel = pagedScreeningMovieResourcesAssembler.toModel(screeningsPage, screeningModelAssembler,linkTo(methodOn(CustomerController.class).getActiveScreeningsPage(page,size,sort)).withSelfRel());
        for (ScreeningModelDto screening : pagedModel) {
            screening.add(linkTo(methodOn(CustomerController.class).getSingleScreening(screening.getId())).withSelfRel());
            screening.add(linkTo(methodOn(CustomerController.class).getSingleMovie(screening.getMovieId())).withRel("movie"));
        }

        return pagedModel;
    }

    @GetMapping("screenings/{screeningId}")
    public ScreeningModelDto getSingleScreening(@PathVariable String screeningId) throws CustomCinemaException {
        ScreeningModelDto screening = screeningModelAssembler.toModel(customerService.getSingleScreening(screeningId));
        screening.add(linkTo(methodOn(CustomerController.class).getSingleScreening(screeningId)).withSelfRel());
        screening.add(linkTo(methodOn(CustomerController.class).getSingleMovie(screening.getMovieId())).withRel("movie"));
        return screening;
    }

    @GetMapping("screenings/by/movie")
    PagedModel<ScreeningModelDto> getActiveScreeningsPageByMovie(@RequestParam(defaultValue = "0", required = false) int page, @RequestParam(defaultValue = "5", required = false) int size, @RequestParam String movieName, @RequestParam(defaultValue = "screenTime", required = false) String sort) throws CustomCinemaException {
        Page<Screening> screenings = customerService.getActiveScreeningsPageByMovie(page, size, movieName, sort);
        PagedModel<ScreeningModelDto> screeningModelDtos = pagedScreeningMovieResourcesAssembler.toModel(screenings, screeningModelAssembler,linkTo(methodOn(CustomerController.class).getActiveMoviesPage(0,5,sort)).withSelfRel());

        for (ScreeningModelDto screening : screeningModelDtos) {
            screening.add(linkTo(methodOn(CustomerController.class).getSingleScreening(screening.getId())).withSelfRel());
            screening.add(linkTo(methodOn(CustomerController.class).getSingleMovie(screening.getMovieId())).withRel("movie"));
        }
        return screeningModelDtos;
    }

    @GetMapping("movies")
    PagedModel<MovieModelDto> getActiveMoviesPage(@RequestParam(defaultValue = "0", required = false) int page, @RequestParam(defaultValue = "5", required = false) int pageSize, @RequestParam(defaultValue = "name", required = false) String sortBy) throws CustomCinemaException {
        Page<Movie> moviePage = customerService.getActiveMoviesPage(page, pageSize, sortBy);
        PagedModel<MovieModelDto> pagedModel = pagedMovieResourcesAssembler.toModel(moviePage, movieModelAssembler);
        for (MovieModelDto movieModelDto : pagedModel) {
            movieModelDto.add(linkTo(methodOn(CustomerController.class).getSingleMovie(movieModelDto.getId())).withSelfRel());
            movieModelDto.add(linkTo(methodOn(CustomerController.class).getActiveScreeningsPageByMovie(0, pageSize, movieModelDto.getName(), "")).withSelfRel());
        }

        return pagedModel;
    }

}

package com.jb.MovieTheater.controller;

import com.jb.MovieTheater.assemblers.*;
import com.jb.MovieTheater.beans.mongo.Movie;
import com.jb.MovieTheater.beans.mongo.Purchase;
import com.jb.MovieTheater.beans.mongo.Screening;
import com.jb.MovieTheater.beans.mysql.Clerk;
import com.jb.MovieTheater.beans.mysql.Customer;
import com.jb.MovieTheater.exception.CustomCinemaException;
import com.jb.MovieTheater.models.movie.MovieModelDao;
import com.jb.MovieTheater.models.movie.MovieModelDto;
import com.jb.MovieTheater.models.screening.ScreeningModelDao;
import com.jb.MovieTheater.models.screening.ScreeningModelDto;
import com.jb.MovieTheater.models.theater.TheaterModelDao;
import com.jb.MovieTheater.models.theater.TheaterModelDto;
import com.jb.MovieTheater.models.ticket.TicketModelDto;
import com.jb.MovieTheater.models.user.ClerkModelDao;
import com.jb.MovieTheater.models.user.ClerkModelDto;
import com.jb.MovieTheater.models.user.CustomerModelDto;
import com.jb.MovieTheater.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final PagedResourcesAssembler<Purchase> pagedPurchaseResourcesAssembler;

    private final PagedResourcesAssembler<Movie> pagedMovieResourcesAssembler;
    private final PagedResourcesAssembler<Screening> pagedScreeningMovieResourcesAssembler;
    private final PagedResourcesAssembler<Customer> pagedCustomerResourcesAssembler;
    private final PagedResourcesAssembler<Clerk> pagedClerkResourcesAssembler;
    private final TicketModelAssembler ticketModelAssembler;
    private final CustomerModelAssembler customerModelAssembler;
    private final MovieModelAssembler movieModelAssembler;
    private final ScreeningModelAssembler screeningModelAssembler;
    private final TheaterModelAssembler theaterModelAssembler;
    private final ClerkModelAssembler clerkModelAssembler;

    private final String defaultPageSize="5";


    @PostMapping("theaters")
    @ResponseStatus(HttpStatus.CREATED)
    public TheaterModelDto addTheater(@RequestBody TheaterModelDao theater) throws CustomCinemaException {
        TheaterModelDto theaterModelDto = theaterModelAssembler.toModel(adminService.addTheater(theater));

        return theaterModelDto;
        //todo add get single theater
    }
    @PostMapping("movies")
    @ResponseStatus(HttpStatus.CREATED)
    public MovieModelDto addMovie(@RequestBody MovieModelDao movie) throws CustomCinemaException {
        MovieModelDto movieModelDto = movieModelAssembler.toModel(adminService.addMovie(movie));
        movieModelDto.add(linkTo(methodOn(AdminController.class).getSingleMovie(movieModelDto.getId())).withSelfRel());
        return movieModelDto;
    }
    @PostMapping("screenings")
    @ResponseStatus(HttpStatus.CREATED)
    public ScreeningModelDto addScreening(@RequestBody ScreeningModelDao screening) throws CustomCinemaException {
        ScreeningModelDto screeningModelDto = screeningModelAssembler.toModel(adminService.addScreening(screening));
        screeningModelDto.add(linkTo(methodOn(AdminController.class).getSingleScreening(screeningModelDto.getId())).withSelfRel());
        return
                screeningModelDto;
    }
    @PostMapping("clerks")
    @ResponseStatus(HttpStatus.CREATED)
    public ClerkModelDto addClerk(@RequestBody ClerkModelDao clerk) throws CustomCinemaException {
        ClerkModelDto clerkModelDto = clerkModelAssembler.toModel(adminService.addClerk(clerk));
        clerkModelDto.add(linkTo(methodOn(AdminController.class).getSingleClerk(clerkModelDto.getId())).withSelfRel());
        return clerkModelDto;
    }
    @PutMapping("theaters/{theaterId}")
    public TheaterModelDto updateTheater(@RequestBody TheaterModelDao theater,@PathVariable String theaterId) throws CustomCinemaException {
        return theaterModelAssembler.toModel(adminService.updateTheater(theater, theaterId));
    }
    @PutMapping("movies/{movieId}")
    public MovieModelDto updateMovie(@RequestBody MovieModelDao movie,@PathVariable String movieId) throws CustomCinemaException {
        MovieModelDto movieModelDto = movieModelAssembler.toModel(adminService.updateMovie(movie, movieId));
        movieModelDto.add(linkTo(methodOn(AdminController.class).getSingleMovie(movieModelDto.getId())).withSelfRel());

        return movieModelDto;
    }

    @PutMapping("clerks/{clerkId}")
    public ClerkModelDto updateClerk(@RequestBody ClerkModelDao clerk,@PathVariable int clerkId) throws CustomCinemaException {
        ClerkModelDto clerkModelDto = clerkModelAssembler.toModel(adminService.updateClerk(clerk, clerkId));
        clerkModelDto.add(linkTo(methodOn(AdminController.class).getSingleClerk(clerkModelDto.getId())).withSelfRel());

        return clerkModelDto;
    }

    @PutMapping("movies/inactivate/{movieId}")
    public MovieModelDto inactivateMovie(@PathVariable String movieId) throws CustomCinemaException {
        MovieModelDto movieModelDto = movieModelAssembler.toModel(adminService.inactivateMovie(movieId));
        movieModelDto.add(linkTo(methodOn(AdminController.class).getSingleMovie(movieModelDto.getId())).withSelfRel());
        movieModelDto.add(linkTo(methodOn(AdminController.class).getScreeningPageByMovieId(0, 20, null, movieModelDto.getId())).withSelfRel());
        return movieModelDto;
    }
    @PutMapping("screenings/inactivate/{screeningId}")
    public ScreeningModelDto inactivateScreening(@PathVariable String screeningId) throws CustomCinemaException {
        ScreeningModelDto screeningModelDto = screeningModelAssembler.toModel(adminService.inactivateScreening(screeningId));
        screeningModelDto.add(linkTo(methodOn(AdminController.class).getSingleScreening(screeningModelDto.getId())).withSelfRel());
        screeningModelDto.add(linkTo(methodOn(AdminController.class).getSingleMovie(screeningModelDto.getMovieId())).withRel("movie"));
        return screeningModelDto;
    }
    @DeleteMapping("clerks/{clerkId}")
    public void deleteClerk(@PathVariable int clerkId) throws CustomCinemaException {
        adminService.deleteClerk(clerkId);
    }
    @GetMapping("customers")
    public PagedModel<CustomerModelDto> getCustomerPage(@RequestParam(defaultValue = "0",required = false) int page, @RequestParam(required = false,defaultValue = defaultPageSize) int size, @RequestParam(required = false,defaultValue = "name") String sort) throws CustomCinemaException {
        Page<Customer> customerPage = adminService.getCustomerPage(page, size, sort);
        PagedModel<CustomerModelDto> customerModelDtos = pagedCustomerResourcesAssembler.toModel(customerPage, customerModelAssembler
        ,linkTo(methodOn(AdminController.class).getCustomerPage(page, size, sort)).withSelfRel());
        for (CustomerModelDto customerModelDto : customerModelDtos) {
            customerModelDto.add(linkTo(methodOn(AdminController.class).getSingleCustomer(customerModelDto.getId())).withSelfRel());
        }
        return customerModelDtos;
    }
    @GetMapping("customers/{customerId}")
    public CustomerModelDto getSingleCustomer(@PathVariable int customerId) throws CustomCinemaException {
        CustomerModelDto customerModelDto = customerModelAssembler.toModel(adminService.getSingleCustomer(customerId));
        customerModelDto.add(linkTo(methodOn(AdminController.class).getSingleCustomer(customerModelDto.getId())).withSelfRel());
        return customerModelDto;
    }
    @GetMapping("clerks")
    public PagedModel<ClerkModelDto> getClerksPage(@RequestParam(defaultValue = "0",required = false) int page, @RequestParam(required = false,defaultValue = defaultPageSize) int size, @RequestParam(required = false,defaultValue = "name") String sort) throws CustomCinemaException {
        Page<Clerk> clerkPage = adminService.getClerksPage(page, size, sort);
        PagedModel<ClerkModelDto> clerkModelDtos = pagedClerkResourcesAssembler.toModel(clerkPage, clerkModelAssembler
        ,linkTo(methodOn(AdminController.class).getClerksPage(page,size,sort)).withSelfRel());
        for (ClerkModelDto clerkModelDto : clerkModelDtos) {
            clerkModelDto.add(linkTo(methodOn(AdminController.class).getSingleClerk(clerkModelDto.getId())).withSelfRel());
        }
        return clerkModelDtos;
    }


    @GetMapping("clerks/{clerkId}")
    public ClerkModelDto getSingleClerk(@PathVariable int clerkId) throws CustomCinemaException {
        ClerkModelDto clerkModelDto = clerkModelAssembler.toModel(adminService.getSingleClerk(clerkId));
        clerkModelDto.add(linkTo(methodOn(AdminController.class).getSingleClerk(clerkModelDto.getId())).withSelfRel());
        return clerkModelDto;
    }

    @GetMapping("movies")
    public PagedModel<MovieModelDto> getMoviePage(@RequestParam(defaultValue = "0",required = false) int page, @RequestParam(required = false,defaultValue = defaultPageSize) int size, @RequestParam(required = false,defaultValue = "name") String sort) throws CustomCinemaException {
        Page<Movie> moviePage = adminService.getMoviePage(page, size, sort);
        PagedModel<MovieModelDto> pagedModel = pagedMovieResourcesAssembler.toModel(moviePage, movieModelAssembler
        ,linkTo(methodOn(AdminController.class).getMoviePage(page,size,sort)).withSelfRel());
        for (MovieModelDto movie : pagedModel) {
            movie.add(linkTo(methodOn(AdminController.class).getSingleMovie(movie.getId())).withSelfRel());
            movie.add(linkTo(methodOn(AdminController.class).getScreeningPageByMovieId(0, size, movie.getId(), null)).withSelfRel());
        }
        return pagedModel;
    }

    @GetMapping("screenings/by/movie/{movieId}")
    public PagedModel<ScreeningModelDto> getScreeningPageByMovieId(@RequestParam(defaultValue = "0",required = false) int page, @RequestParam(required = false,defaultValue = defaultPageSize) int size, @PathVariable String movieId, @RequestParam(required = false,defaultValue = "name") String sort) throws CustomCinemaException {
        Page<Screening> screeningPage = adminService.getScreeningsPageByMovie(page, size, movieId, sort);
        PagedModel<ScreeningModelDto> screeningModelDtos = pagedScreeningMovieResourcesAssembler.toModel(screeningPage, screeningModelAssembler
        ,linkTo(methodOn(AdminController.class).getScreeningPageByMovieId(page,size,movieId,sort)).withSelfRel());
        for (ScreeningModelDto screening : screeningModelDtos) {
            screening.add(linkTo(methodOn(AdminController.class).getSingleScreening(screening.getId())).withSelfRel());
            screening.add(linkTo(methodOn(AdminController.class).getSingleMovie(screening.getMovieId())).withRel("movie"));
        }
        return screeningModelDtos;
    }

    @GetMapping("movies/{movieId}")
    public MovieModelDto getSingleMovie(@PathVariable String movieId) throws CustomCinemaException {
        MovieModelDto movie = movieModelAssembler.toModel(adminService.getSingleMovie(movieId));
        movie.add(linkTo(methodOn(AdminController.class).getSingleMovie(movie.getId())).withSelfRel());
        movie.add(linkTo(methodOn(AdminController.class).getScreeningPageByMovieId(0, 20, movie.getId(), null)).withRel("movie"));
        return movie;
    }
    @GetMapping("screenings")
    public PagedModel<ScreeningModelDto> getScreeningPage(@RequestParam(defaultValue = "0",required = false) int page, @RequestParam(required = false,defaultValue = defaultPageSize) int size, @RequestParam(required = false,defaultValue = "screenTime") String sort) throws CustomCinemaException {
        Page<Screening> screeningPage = adminService.getScreeningPage(page, size, sort);
        PagedModel<ScreeningModelDto> screenings = pagedScreeningMovieResourcesAssembler.toModel(screeningPage, screeningModelAssembler
        ,linkTo(methodOn(AdminController.class).getScreeningPage(page, size, sort)).withSelfRel());
        for (ScreeningModelDto screening : screenings) {
            screening.add(linkTo(methodOn(AdminController.class).getSingleScreening(screening.getId())).withSelfRel());
            screening.add(linkTo(methodOn(AdminController.class).getSingleMovie(screening.getMovieId())).withRel("movie"));
        }
        return screenings;
    }
    @GetMapping("screenings/{screeningId}")
    ScreeningModelDto getSingleScreening(@PathVariable String screeningId) throws CustomCinemaException {
        ScreeningModelDto screening = screeningModelAssembler.toModel(adminService.getSingleScreening(screeningId));
        screening.add(linkTo(methodOn(AdminController.class).getSingleScreening(screening.getId())).withSelfRel());
        screening.add(linkTo(methodOn(AdminController.class).getSingleMovie(screening.getMovieId())).withRel("movie"));
        return screening;
    }
    @GetMapping("tickets")
    PagedModel<TicketModelDto> getPurchasePage(@RequestParam(defaultValue = "0",required = false) int page, @RequestParam(required = false,defaultValue = defaultPageSize) int size, @RequestParam(required = false,defaultValue = "purchaseTime") String sort) throws CustomCinemaException {
        Page<Purchase> purchasePage = adminService.getPurchasePage(page, size, sort);
        PagedModel<TicketModelDto> tickets = pagedPurchaseResourcesAssembler.toModel(purchasePage, ticketModelAssembler
        ,linkTo(methodOn(AdminController.class).getPurchasePage(page, size, sort)).withSelfRel());
        for (TicketModelDto ticket : tickets) {
            ticket.add(linkTo(methodOn(AdminController.class).getSinglePurchase(ticket.getId())).withSelfRel());
            ticket.add(linkTo(methodOn(AdminController.class).getSingleCustomer(ticket.getUserId())).withRel("customer"));
        }
        return tickets;
    }
    @GetMapping("tickets/{purchaseId}")
    TicketModelDto getSinglePurchase(@PathVariable String purchaseId) throws CustomCinemaException {
        TicketModelDto ticket = ticketModelAssembler.toModel(adminService.getSinglePurchase(purchaseId));
        ticket.add(linkTo(methodOn(AdminController.class).getSinglePurchase(ticket.getId())).withSelfRel());
        ticket.add(linkTo(methodOn(AdminController.class).getSingleCustomer(ticket.getUserId())).withRel("customer"));
        return ticket;
    }

}

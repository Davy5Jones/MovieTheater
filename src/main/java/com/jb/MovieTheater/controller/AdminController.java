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
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("api/admin")
@RequiredArgsConstructor
//@PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
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

    private final String defaultPageSize = "5";


    @PostMapping("theaters")
    @ResponseStatus(HttpStatus.CREATED)
    public TheaterModelDto addTheater(@RequestBody @Validated TheaterModelDao theater) throws CustomCinemaException {

        return theaterModelAssembler.toModel(adminService.addTheater(theater));
        //todo add get single theater
    }

    @PostMapping("movies")
    @ResponseStatus(HttpStatus.CREATED)
    public MovieModelDto addMovie(@RequestBody @Validated MovieModelDao movie) throws CustomCinemaException {
        MovieModelDto movieModelDto = movieModelAssembler.toModel(adminService.addMovie(movie));
        movieModelDto.add(linkTo(methodOn(AdminController.class).getSingleMovie(movieModelDto.getId())).withSelfRel());
        return movieModelDto;
    }

    @PostMapping("screenings")
    @ResponseStatus(HttpStatus.CREATED)
    public ScreeningModelDto addScreening(@RequestBody @Validated ScreeningModelDao screening) throws CustomCinemaException {
        ScreeningModelDto screeningModelDto = screeningModelAssembler.toModel(adminService.addScreening(screening));
        screeningModelDto.add(linkTo(methodOn(AdminController.class).getSingleScreening(screeningModelDto.getId())).withSelfRel());
        return
                screeningModelDto;
    }

    @PostMapping("clerks")
    @ResponseStatus(HttpStatus.CREATED)
    public ClerkModelDto addClerk(@RequestBody @Validated ClerkModelDao clerk) throws CustomCinemaException {
        ClerkModelDto clerkModelDto = clerkModelAssembler.toModel(adminService.addClerk(clerk));
        clerkModelDto.add(linkTo(methodOn(AdminController.class).getSingleClerk(clerkModelDto.getId())).withSelfRel());
        return clerkModelDto;
    }

    @PutMapping("theaters/{theaterId}")
    public TheaterModelDto updateTheater(@RequestBody @Validated TheaterModelDao theater, @PathVariable String theaterId) throws CustomCinemaException {
        return theaterModelAssembler.toModel(adminService.updateTheater(theater, theaterId));
    }

    @PutMapping("movies/{movieId}")
    public MovieModelDto updateMovie(@RequestBody @Validated MovieModelDao movie, @PathVariable String movieId) throws CustomCinemaException {
        MovieModelDto movieModelDto = movieModelAssembler.toModel(adminService.updateMovie(movie, movieId));
        movieModelDto.add(linkTo(methodOn(AdminController.class).getSingleMovie(movieModelDto.getId())).withSelfRel());
        return movieModelDto;
    }

    @PutMapping("clerks/{clerkId}")
    public ClerkModelDto updateClerk(@RequestBody @Validated ClerkModelDao clerk, @PathVariable int clerkId) throws CustomCinemaException {
        ClerkModelDto clerkModelDto = clerkModelAssembler.toModel(adminService.updateClerk(clerk, clerkId));
        clerkModelDto.add(linkTo(methodOn(AdminController.class).getSingleClerk(clerkModelDto.getId())).withSelfRel());

        return clerkModelDto;
    }

    @PutMapping("movies/inactivate/{movieId}")
    public MovieModelDto inactivateMovie(@PathVariable String movieId) throws CustomCinemaException {
        MovieModelDto movieModelDto = movieModelAssembler.toModel(adminService.inactivateMovie(movieId));
        movieModelDto.add(linkTo(methodOn(AdminController.class).getSingleMovie(movieModelDto.getId())).withSelfRel());
        movieModelDto.add(linkTo(methodOn(AdminController.class).getScreeningPageByMovieId(0, 20, movieModelDto.getId(), "",Sort.Direction.ASC)).withSelfRel());
        return movieModelDto;
    }

    @DeleteMapping("clerks/{clerkId}")
    public void deleteClerk(@PathVariable int clerkId) throws CustomCinemaException {
        adminService.deleteClerk(clerkId);
    }

    @GetMapping("customers")
    public PagedModel<CustomerModelDto> getCustomerPage
            (@RequestParam(defaultValue = "0", required = false) int page,
             @RequestParam(required = false, defaultValue = defaultPageSize) int size,
             @RequestParam(required = false, defaultValue = "name") String sort,
             @RequestParam(required = false, defaultValue = "ASC") Sort.Direction order) throws CustomCinemaException {
        Page<Customer> customerPage = adminService.getCustomerPage(page, size, sort.split(",")[0],order);
        PagedModel<CustomerModelDto> customerModelDtos = pagedCustomerResourcesAssembler.toModel(customerPage, customerModelAssembler);
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
    public PagedModel<ClerkModelDto> getClerksPage
            (@RequestParam(defaultValue = "0", required = false) int page,
             @RequestParam(required = false, defaultValue = defaultPageSize) int size,
             @RequestParam(required = false, defaultValue = "name") String sort,
             @RequestParam(required = false, defaultValue = "ASC") Sort.Direction order) throws CustomCinemaException {
        Page<Clerk> clerkPage = adminService.getClerksPage(page, size, sort.split(",")[0],order);
        PagedModel<ClerkModelDto> clerkModelDtos = pagedClerkResourcesAssembler.toModel(clerkPage, clerkModelAssembler);
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
    public PagedModel<MovieModelDto> getMoviePage(@RequestParam(defaultValue = "0"
            , required = false) int page
            , @RequestParam(required = false, defaultValue = defaultPageSize) int size
            , @RequestParam(required = false, defaultValue = "name") String sort
            ,@RequestParam(required = false, defaultValue = "ASC") Sort.Direction order) throws CustomCinemaException {
        System.out.println(order);
        System.out.println(sort);
        Page<Movie> moviePage = adminService.getMoviePage(page, size, sort.split(",")[0],order);
        System.out.println(sort+"@@@");

        PagedModel<MovieModelDto> pagedModel = pagedMovieResourcesAssembler.toModel(moviePage, movieModelAssembler);
        for (MovieModelDto movie : pagedModel) {
            movie.add(linkTo(methodOn(AdminController.class).getSingleMovie(movie.getId())).withSelfRel());
            movie.add(linkTo(methodOn(AdminController.class).getScreeningPageByMovieId(0, size, movie.getId(), "",Sort.Direction.ASC)).withSelfRel());
        }
        return pagedModel;
    }

    @GetMapping("screenings/by/movie/{movieId}")
    public PagedModel<ScreeningModelDto> getScreeningPageByMovieId
            (@RequestParam(defaultValue = "0", required = false) int page,
             @RequestParam(required = false, defaultValue = defaultPageSize) int size,
             @PathVariable String movieId,
             @RequestParam(required = false, defaultValue = "screenTime") String sort,
            @RequestParam(required = false, defaultValue = "ASC") Sort.Direction order) throws CustomCinemaException {
        Page<Screening> screeningPage = adminService.getActiveScreeningsPageByMovie(page, size, movieId, sort.split(",")[0],order);
        PagedModel<ScreeningModelDto> screeningModelDtos = pagedScreeningMovieResourcesAssembler.toModel(screeningPage, screeningModelAssembler);
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
        movie.add(linkTo(methodOn(AdminController.class).getScreeningPageByMovieId(0, Integer.parseInt(defaultPageSize), movie.getId(), "",Sort.Direction.ASC)).withRel("movie"));
        return movie;
    }

    @GetMapping("screenings")
    public PagedModel<ScreeningModelDto> getScreeningPage
            (@RequestParam(defaultValue = "0", required = false) int page,
             @RequestParam(required = false, defaultValue = defaultPageSize) int size
                    , @RequestParam(required = false, defaultValue = "screenTime") String sort
                    ,@RequestParam(required = false, defaultValue = "ASC") Sort.Direction order) throws CustomCinemaException {
        Page<Screening> screeningPage = adminService.getScreeningPage(page, size,sort.split(",")[0],order);
        PagedModel<ScreeningModelDto> screenings = pagedScreeningMovieResourcesAssembler.toModel(screeningPage, screeningModelAssembler);
        for (ScreeningModelDto screening : screenings) {
            screening.add(linkTo(methodOn(AdminController.class).getSingleScreening(screening.getId())).withSelfRel());
            screening.add(linkTo(methodOn(AdminController.class).getSingleMovie(screening.getMovieId())).withRel("movie"));
        }
        return screenings;
    }

    @GetMapping("screenings/{screeningId}")
    public ScreeningModelDto getSingleScreening(@PathVariable String screeningId) throws CustomCinemaException {
        ScreeningModelDto screening = screeningModelAssembler.toModel(adminService.getSingleScreening(screeningId));
        screening.add(linkTo(methodOn(AdminController.class).getSingleScreening(screening.getId())).withSelfRel());
        screening.add(linkTo(methodOn(AdminController.class).getSingleMovie(screening.getMovieId())).withRel("movie"));
        return screening;
    }

    @GetMapping("purchases")
    public PagedModel<TicketModelDto> getPurchasePage
            (@RequestParam(defaultValue = "0", required = false) int page,
             @RequestParam(required = false, defaultValue = defaultPageSize) int size,
             @RequestParam(required = false, defaultValue = "purchaseTime") String sort,
             @RequestParam(required = false, defaultValue = "ASC") Sort.Direction order) throws CustomCinemaException {
        Page<Purchase> purchasePage = adminService.getPurchasePage(page, size, sort.split(",")[0],order);
        PagedModel<TicketModelDto> tickets = pagedPurchaseResourcesAssembler.toModel(purchasePage, ticketModelAssembler);
        for (TicketModelDto ticket : tickets) {
            ticket.add(linkTo(methodOn(AdminController.class).getSinglePurchase(ticket.getId())).withSelfRel());
            ticket.add(linkTo(methodOn(AdminController.class).getSingleCustomer(ticket.getUserId())).withRel("customer"));
        }
        return tickets;
    }

    @GetMapping("purchases/by/customer")
    public PagedModel<TicketModelDto> getPurchasePageByCustomer
            (@RequestParam (defaultValue = "0") int id,
                    @RequestParam(defaultValue = "0", required = false) int page,
             @RequestParam(required = false, defaultValue = defaultPageSize) int size,
             @RequestParam(required = false, defaultValue = "purchaseTime") String sort,
             @RequestParam(required = false, defaultValue = "ASC") Sort.Direction order) throws CustomCinemaException {
        Page<Purchase> purchasePage = adminService.getPurchasePageByCustomer(id,page, size, sort.split(",")[0],order);
        PagedModel<TicketModelDto> tickets = pagedPurchaseResourcesAssembler.toModel(purchasePage, ticketModelAssembler);
        for (TicketModelDto ticket : tickets) {
            ticket.add(linkTo(methodOn(AdminController.class).getSinglePurchase(ticket.getId())).withSelfRel());
            ticket.add(linkTo(methodOn(AdminController.class).getSingleCustomer(ticket.getUserId())).withRel("customer"));
        }
        return tickets;
    }

    @GetMapping("purchases/{purchaseId}")
    public TicketModelDto getSinglePurchase(@PathVariable String purchaseId) throws CustomCinemaException {
        TicketModelDto ticket = ticketModelAssembler.toModel(adminService.getSinglePurchase(purchaseId));
        ticket.add(linkTo(methodOn(AdminController.class).getSinglePurchase(ticket.getId())).withSelfRel());
        ticket.add(linkTo(methodOn(AdminController.class).getSingleCustomer(ticket.getUserId())).withRel("customer"));
        return ticket;
    }

    @GetMapping("theaters/names")
    public CollectionModel<String> getTheaterNames() {
        CollectionModel<String> list = CollectionModel.of(adminService.getTheaterNames());
        list.add(linkTo(methodOn(AdminController.class).getTheaterNames()).withSelfRel());
        return list;
    }

}

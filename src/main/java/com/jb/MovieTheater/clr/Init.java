package com.jb.MovieTheater.clr;

import com.jb.MovieTheater.beans.mongo.Category;
import com.jb.MovieTheater.beans.mongo.Movie;
import com.jb.MovieTheater.beans.mongo.Screening;
import com.jb.MovieTheater.beans.mongo.Theater;
import com.jb.MovieTheater.models.screening.ScreeningModelDao;
import com.jb.MovieTheater.models.ticket.TicketModelDao;
import com.jb.MovieTheater.models.user.ClerkModelDao;
import com.jb.MovieTheater.models.user.CustomerModelDao;
import com.jb.MovieTheater.repos.ClerkRepository;
import com.jb.MovieTheater.repos.movie.MovieRepository;
import com.jb.MovieTheater.repos.purchase.PurchaseRepository;
import com.jb.MovieTheater.repos.screening.ScreeningRepository;
import com.jb.MovieTheater.repos.theater.TheaterRepository;
import com.jb.MovieTheater.services.AdminService;
import com.jb.MovieTheater.services.CustomerService;
import com.jb.MovieTheater.services.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Init implements CommandLineRunner {

    private final MovieRepository movieRepository;
    private final TheaterRepository theaterRepository;
    private final ScreeningRepository screeningRepository;
    private final PurchaseRepository purchaseRepository;
    private final AdminService adminService;
    private final HomeService homeService;
    private final CustomerService customerService;
    private final ClerkRepository clerkRepository;


    @Override
    public void run(String... args) throws Exception {

        Movie movie = movieRepository.save(Movie.builder()
                .rating(4)
                .name("spiderman")
                .description("spiderman saves")
                .category(Category.ACTION)
                .isActive(true)
                .duration(120)
                .build());
        Movie movie1 = movieRepository.save(Movie.builder()
                .rating(3)
                .name("aquaman")
                .description("aquaman saves")
                .category(Category.FICTION)
                .isActive(true)
                .duration(140)
                .build());
        Movie movie2 = movieRepository.save(Movie.builder()
                .rating(4)
                .name("Inglorious bastards")
                .description("bastards saves")
                .category(Category.HORROR)
                .isActive(true)
                .duration(120)
                .build());
        Movie movie3 = movieRepository.save(Movie.builder()
                .rating(4)
                .name("Annabel")
                .description("Annabel revenge")
                .category(Category.HORROR)
                .isActive(true)
                .duration(85)
                .build());
        Movie movie4 = movieRepository.save(Movie.builder()
                .rating(4)
                .name("soprano")
                .description("mob movie")
                .category(Category.THRILLER)
                .isActive(true)
                .duration(94)
                .build());
        Movie movie5 = movieRepository.save(Movie.builder()
                .rating(5)
                .name("ex machina")
                .description("robot goes mad")
                .category(Category.FICTION)
                .isActive(true)
                .duration(103)
                .build());
        Movie movie6 = movieRepository.save(Movie.builder()
                .rating(2.5F)
                .name("Adam")
                .description("adam sandler movie saves")
                .category(Category.COMEDY)
                .isActive(true)
                .duration(98)
                .build());
        Theater theater = theaterRepository.save(Theater.builder()
                .rows(List.of(5, 4, 6, 5, 4))
                .name("A1")
                .build());
        Theater theater2 = theaterRepository.save(Theater.builder()
                .rows(List.of(6, 7, 6, 8))
                .name("B1")
                .build());
        Theater theater3 = theaterRepository.save(Theater.builder()
                .rows(List.of(5, 8, 6, 3))
                .name("C1")
                .build());
        Screening screening = adminService.addScreening(new ScreeningModelDao(movie.getName(), Instant.now().plusSeconds(60 * 60), theater.getId(), true));
        Screening screening2 = adminService.addScreening(new ScreeningModelDao(movie2.getName(), Instant.now().plusSeconds(60 * 60 * 20), theater2.getId(), true));

        Screening screening3 = adminService.addScreening(new ScreeningModelDao(movie3.getName(), Instant.now().plusSeconds(60 * 60 * 5), theater.getId(), true));
        Screening screening4 = adminService.addScreening(new ScreeningModelDao(movie4.getName(), Instant.now().plusSeconds(60 * 60 * 24 * 6), theater3.getId(), false));

        Screening screening5 = adminService.addScreening(new ScreeningModelDao(movie5.getName(), Instant.now().plusSeconds(60 * 60 * 24 * 2), theater.getId(), true));

        Screening screening6 = adminService.addScreening(new ScreeningModelDao(movie6.getName(), Instant.now().plusSeconds(60 * 60 * 12), theater3.getId(), false));


        homeService.register(new CustomerModelDao("ido@gmail.com", "ido", "1234"));
        homeService.register(new CustomerModelDao("niv@gmail.com", "niv", "1234"));

        customerService.purchaseTicket(new TicketModelDao(screening.getId(), 0, 1), 1);
        customerService.purchaseTicket(new TicketModelDao(screening.getId(), 2, 2), 1);
        customerService.purchaseTicket(new TicketModelDao(screening.getId(), 1, 2), 1);
        customerService.purchaseTicket(new TicketModelDao(screening.getId(), 3, 3), 1);
        customerService.purchaseTicket(new TicketModelDao(screening.getId(), 1, 3), 1);
        customerService.purchaseTicket(new TicketModelDao(screening.getId(), 0, 3), 1);


        customerService.purchaseTicket(new TicketModelDao(screening.getId(), 2, 1), 2);
        customerService.purchaseTicket(new TicketModelDao(screening.getId(), 3, 0), 2);

        adminService.addClerk(new ClerkModelDao("bob@gmail.co", "bob", "1234"));

        System.out.println(clerkRepository.existsByEmail("bob@gmail.co")
        );
    }

}

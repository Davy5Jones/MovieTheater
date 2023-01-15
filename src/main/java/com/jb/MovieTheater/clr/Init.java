package com.jb.MovieTheater.clr;

import com.jb.MovieTheater.beans.mongo.Category;
import com.jb.MovieTheater.beans.mongo.Movie;
import com.jb.MovieTheater.beans.mongo.Screening;
import com.jb.MovieTheater.beans.mongo.Theater;
import com.jb.MovieTheater.models.screening.ScreeningModelDao;
import com.jb.MovieTheater.models.ticket.TicketModelDao;
import com.jb.MovieTheater.models.user.CustomerModelDao;
import com.jb.MovieTheater.repos.CustomerRepository;
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
    private  final HomeService homeService;
    private final CustomerService customerService;


    @Override
    public void run(String... args) throws Exception {

        Movie movie= movieRepository.save(Movie.builder()
                        .rating(4)
                        .name("spiderman")
                        .description("spiderman saves")
                        .category(Category.ACTION)
                        .isActive(true)
                        .duration(120)
                .build());
        Theater theater = theaterRepository.save(Theater.builder()
                        .rows(List.of(5,4,3,2))
                        .name("A1")
                .build())
        ;
        Screening screening=adminService.addScreening(new ScreeningModelDao(movie.getName(), Instant.now().plusSeconds(60*60),theater.getId(),true));
        homeService.register(new CustomerModelDao("ido@gmail.com","ido","1234"));

        customerService.purchaseTicket(new TicketModelDao(screening.getId(),0,0),1);
    }

}

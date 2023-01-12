package com.jb.MovieTheater.clr;

import com.jb.MovieTheater.beans.mongo.Category;
import com.jb.MovieTheater.beans.mongo.Movie;
import com.jb.MovieTheater.beans.mongo.Screening;
import com.jb.MovieTheater.beans.mongo.Theater;
import com.jb.MovieTheater.beans.mysql.Customer;
import com.jb.MovieTheater.models.screening.ScreeningModelDao;
import com.jb.MovieTheater.models.ticket.TicketModelDao;
import com.jb.MovieTheater.repos.CustomerRepository;
import com.jb.MovieTheater.repos.movie.MovieRepository;
import com.jb.MovieTheater.repos.purchase.PurchaseRepository;
import com.jb.MovieTheater.repos.screening.ScreeningRepository;
import com.jb.MovieTheater.repos.theater.TheaterRepository;
import com.jb.MovieTheater.services.AdminService;
import com.jb.MovieTheater.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Init implements CommandLineRunner {
    private final PurchaseRepository purchaseRepository;
    private final ScreeningRepository screeningRepository;
    private final CustomerRepository customerRepository;
    private final MovieRepository movieRepository;
    private final TheaterRepository theaterRepository;
    private final CustomerService customerService;

    private final AdminService adminService;


    @Override
    public void run(String... args) throws Exception {


        customerRepository.save(Customer.builder()
                .name("ido").email("ido@gmail.com")
                .password("1234")
                .build());

        Movie movie = movieRepository.save(Movie.builder()
                .description("spiderman saves the world")
                .name("spiderman 1")
                .category(Category.FICTION)
                .duration(125)
                .isActive(true)
                .rating(4)
                .build());
        customerRepository.save(Customer.builder()
                .name("daisy")
                .password("1234")
                .email("ido@gmail.com")
                .build());


        movieRepository.findTop5ByIsActiveOrderByRating(true).forEach(System.out::println);


        List<Integer> rows = new ArrayList<>();
        rows.add(8);
        rows.add(8);
        rows.add(6);
        rows.add(8);
        rows.add(8);
        Screening screening1 = adminService.addScreening(new ScreeningModelDao(movie.getName(), Instant.now().plusSeconds(60 * 60 * 24), theaterRepository.save(new Theater("A1", rows)).getId(), true));

        customerService.purchaseTicket(TicketModelDao.builder()
                .rowId(0)
                .seatId(0)
                .screeningId(screeningRepository.findAll().get(0).getId())
                .build(), 1);
        Screening screening = screeningRepository.save(Screening.builder()
                .screenTime(Instant.now().minusSeconds(60 * 60 * 24))
                .seats(List.of(new boolean[5], new boolean[4], new boolean[3]))
                .theaterId("1")
                .isActive(true)
                .is3D(true)
                .build());
        screeningRepository.inactivateOldScreenings();

        System.out.println(screeningRepository.getScreeningActive(screening1.getId()));
        theaterRepository.findById(theaterRepository.findAll().get(0).getId());
        theaterRepository.findById(theaterRepository.findAll().get(0).getId());

        theaterRepository.findById(theaterRepository.findAll().get(0).getId());

    }

}

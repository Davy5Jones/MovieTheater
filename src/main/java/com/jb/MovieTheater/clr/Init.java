package com.jb.MovieTheater.clr;

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
    private final HomeService homeService;


    @Override
    public void run(String... args) throws Exception {


    }

}

package com.jb.MovieTheater.services;

import com.jb.MovieTheater.beans.mongo.Movie;
import com.jb.MovieTheater.beans.mongo.Purchase;
import com.jb.MovieTheater.beans.mongo.Screening;
import com.jb.MovieTheater.beans.mongo.Theater;
import com.jb.MovieTheater.beans.mysql.Clerk;
import com.jb.MovieTheater.beans.mysql.Customer;
import com.jb.MovieTheater.exception.CustomCinemaException;
import com.jb.MovieTheater.models.movie.MovieModelDao;
import com.jb.MovieTheater.models.screening.ScreeningModelDao;
import com.jb.MovieTheater.models.theater.TheaterModelDao;
import com.jb.MovieTheater.models.user.ClerkModelDao;
import org.springframework.data.domain.Page;

public interface AdminService {

    Theater addTheater(TheaterModelDao theater) throws CustomCinemaException;

    Movie addMovie(MovieModelDao movie) throws CustomCinemaException;

    Screening addScreening(ScreeningModelDao screening) throws CustomCinemaException;

    Clerk addClerk(ClerkModelDao clerk) throws CustomCinemaException;

    Theater updateTheater(TheaterModelDao theater, String theaterId) throws CustomCinemaException;

    Movie updateMovie(MovieModelDao movie, String movieId) throws CustomCinemaException;

    Clerk updateClerk(ClerkModelDao clerk, int clerkId) throws CustomCinemaException;

    Movie inactivateMovie(String movieId) throws CustomCinemaException;

    Screening inactivateScreening(String screeningId) throws CustomCinemaException;

    void deleteClerk(int clerkId) throws CustomCinemaException;

    Page<Customer> getCustomerPage(int page,int pageSize, String sortBy);
    Customer getSingleCustomer(int customerId) throws CustomCinemaException;


    Page<Clerk> getClerksPage(int page,int pageSize, String sortBy);

    Clerk getSingleClerk(int clerkId) throws CustomCinemaException;

    Page<Movie> getMoviePage(int page,int pageSize, String sortBy);
    Page<Screening> getScreeningsPageByMovie(int page,int pageSize, String movieId, String sortBy);


    Movie getSingleMovie(String movieId) throws CustomCinemaException;

    Page<Screening> getScreeningPage(int page,int pageSize, String sortBy);

    Screening getSingleScreening(String screeningId) throws CustomCinemaException;

    Page<Purchase> getPurchasePage(int page,int pageSize, String sortBy);
    Purchase getSinglePurchase(String purchaseId) throws CustomCinemaException;
}

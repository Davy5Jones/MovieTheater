package com.jb.MovieTheater.services;

import com.jb.MovieTheater.beans.mongo.Movie;
import com.jb.MovieTheater.beans.mongo.Purchase;
import com.jb.MovieTheater.beans.mongo.Screening;
import com.jb.MovieTheater.beans.mysql.Customer;
import com.jb.MovieTheater.exception.CustomCinemaException;
import com.jb.MovieTheater.models.ticket.TicketModelDao;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomerService {

    Purchase purchaseTicket(TicketModelDao ticket, int userId) throws CustomCinemaException;

    Page<Purchase> findAllUserTickets(int page, int pageSize, int customerId);


    Purchase findSingleUserTicket(String purchaseId, int userId) throws CustomCinemaException;

    Customer getCustomerDetails(int customerId) throws CustomCinemaException;

    Page<Screening> getActiveScreeningsPage(int page, int pageSize, String sortBy);

    Screening getSingleScreening(String screeningId) throws CustomCinemaException;

    Page<Screening> getActiveScreeningsPageByMovie(int page, int pageSize, String movieId, String sortBy);

    Page<Movie> getActiveMoviesPage(int page, int pageSize, String sortBy);

    Movie getSingleMovie(String movieId) throws CustomCinemaException;

    //@Cacheable(value = "recommendedMovies")
    List<Movie> getRecommendedMovies();
}

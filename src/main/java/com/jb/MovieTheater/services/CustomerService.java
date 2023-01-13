package com.jb.MovieTheater.services;

import com.jb.MovieTheater.beans.mongo.Movie;
import com.jb.MovieTheater.exception.CustomCinemaException;
import com.jb.MovieTheater.models.PageableModel;
import com.jb.MovieTheater.models.screening.ScreeningModelDto;
import com.jb.MovieTheater.models.ticket.TicketModelDao;
import com.jb.MovieTheater.models.ticket.TicketModelDto;
import com.jb.MovieTheater.models.user.CustomerModelDto;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public interface CustomerService {

    TicketModelDto purchaseTicket(TicketModelDao ticket, int userId) throws CustomCinemaException;

    List<TicketModelDto> findAllUserTickets(int userId);


    TicketModelDto findSingleUserTicket(String purchaseId, int userId) throws CustomCinemaException;

    CustomerModelDto getCustomerDetails(int customerId) throws CustomCinemaException;

    PageableModel<ScreeningModelDto> getActiveScreeningsPage(int page, String sortBy);

    ScreeningModelDto getSingleScreening(String screeningId) throws CustomCinemaException;

    PageableModel<ScreeningModelDto> getActiveScreeningsPageByMovie(int page, String movieName, String sortBy);

    PageableModel<Movie> getActiveMoviesPage(int page, String sortBy);

    Movie getSingleMovie(String movieId) throws CustomCinemaException;

    @Cacheable(value = "recommendedMovies")
    List<Movie> getRecommendedMovies();
}

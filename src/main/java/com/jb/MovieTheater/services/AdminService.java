package com.jb.MovieTheater.services;

import com.jb.MovieTheater.beans.mongo.Movie;
import com.jb.MovieTheater.beans.mongo.Screening;
import com.jb.MovieTheater.beans.mongo.Theater;
import com.jb.MovieTheater.exception.CustomCinemaException;
import com.jb.MovieTheater.models.movie.MovieModelDao;
import com.jb.MovieTheater.models.screening.ScreeningModelDao;
import com.jb.MovieTheater.models.screening.ScreeningModelDto;
import com.jb.MovieTheater.models.theater.TheaterModelDao;
import com.jb.MovieTheater.models.user.ClerkModelDao;
import com.jb.MovieTheater.models.user.ClerkModelDto;
import com.jb.MovieTheater.models.user.CustomerModelDto;

import java.util.List;

public interface AdminService {

    Theater addTheater(TheaterModelDao theater) throws CustomCinemaException;
    Movie addMovie(MovieModelDao movie) throws CustomCinemaException;
    Screening addScreening(ScreeningModelDao screening) throws CustomCinemaException;
    ClerkModelDto addClerk(ClerkModelDao clerk) throws CustomCinemaException;

    Theater updateTheater(TheaterModelDao theater,String theaterId) throws CustomCinemaException;
    Movie updateMovie(MovieModelDao movie,String movieId) throws CustomCinemaException;
    ClerkModelDto updateClerk(ClerkModelDao clerk, int clerkId) throws CustomCinemaException;
    void inactivateMovie(String movieId) throws CustomCinemaException;
    Screening inactivateScreening(String screeningId) throws CustomCinemaException;
    void deleteClerk(int clerkId) throws CustomCinemaException;

    List<CustomerModelDto> getCustomerPage(int page);

    List<ClerkModelDto> getClerksPage(int page);

    List<Movie> getMoviePage(int page);

    List<ScreeningModelDto> getScreeningPage(int page);
}

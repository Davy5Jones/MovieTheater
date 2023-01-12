package com.jb.MovieTheater.repos.movie;

import com.jb.MovieTheater.beans.mongo.Movie;
import com.jb.MovieTheater.exception.CustomCinemaException;
import com.jb.MovieTheater.models.movie.MovieModelDao;

public interface MovieRepositoryTemplate {
    long inactivateMovie(String movieId) throws CustomCinemaException;

    int getMovieDurationByName(String movieName) throws CustomCinemaException;

    String getMovieName(String movieId) throws CustomCinemaException;

    Movie updateMovie(MovieModelDao movieModelDao, String movieId);

}

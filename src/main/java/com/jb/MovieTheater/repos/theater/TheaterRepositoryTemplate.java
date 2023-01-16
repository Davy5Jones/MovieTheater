package com.jb.MovieTheater.repos.theater;

import com.jb.MovieTheater.exception.CustomCinemaException;

public interface TheaterRepositoryTemplate {
    String getTheaterNameById(String theaterId) ;
}

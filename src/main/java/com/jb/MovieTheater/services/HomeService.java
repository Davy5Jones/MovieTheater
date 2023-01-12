package com.jb.MovieTheater.services;

import com.jb.MovieTheater.beans.mongo.Movie;
import com.jb.MovieTheater.models.screening.ScreeningModelDto;

import java.util.List;

public interface HomeService {

    List<ScreeningModelDto> todayScreeningsByTime(int page,int size);

    List<Movie> getRecommendedMovies();

}

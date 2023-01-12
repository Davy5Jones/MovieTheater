package com.jb.MovieTheater.repos.screening;


import com.jb.MovieTheater.beans.mongo.Screening;
import com.jb.MovieTheater.exception.CustomCinemaException;
import com.jb.MovieTheater.models.screening.ScreeningModelDao;

public interface ScreeningRepositoryTemplate {


    void inactivateOldScreenings();
    Screening inactivateScreening(String screeningId);

    void updateScreeningsDurationByMovieName(String movieName,int duration);
}

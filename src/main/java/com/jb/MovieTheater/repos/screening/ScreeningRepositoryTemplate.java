package com.jb.MovieTheater.repos.screening;


import com.jb.MovieTheater.beans.mongo.Screening;

import java.util.Optional;

public interface ScreeningRepositoryTemplate {


    void inactivateOldScreenings();

    Optional<Screening> inactivateScreening(String screeningId);


    void updateScreeningsDurationByMovieId(String movieId, int duration);
}

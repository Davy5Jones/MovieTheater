package com.jb.MovieTheater.repos.theater;

import com.jb.MovieTheater.beans.mongo.Theater;

import java.util.List;

public interface TheaterRepositoryTemplate {
    String getTheaterNameById(String theaterId);
    List<Theater> getAllNames();
}

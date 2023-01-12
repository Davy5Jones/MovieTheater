package com.jb.MovieTheater.repos.movie;

import com.jb.MovieTheater.beans.mongo.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface MovieRepository extends MongoRepository<Movie,String>, MovieRepositoryTemplate {



    boolean existsByName(String name);

    boolean existsByNameAndId(String name, String movieId);

    @Query(value = "{ name : ?0, isActive : true}",exists = true)
    boolean getMovieIsActiveByName(String movieName);

    List<Movie> findTop5ByIsActiveOrderByRating(boolean is);






}

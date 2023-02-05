package com.jb.MovieTheater.repos.movie;

import com.jb.MovieTheater.beans.mongo.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends MongoRepository<Movie, String>, MovieRepositoryTemplate {


    boolean existsByName(String name);

    boolean existsByNameAndId(String name, String movieId);

    @Query(value = "{ id : ?0, isActive : true}", exists = true)
    boolean getMovieIsActiveById(String movieId);

    List<Movie> findTop5ByIsActiveOrderByRatingDesc(boolean is);

    @Query(value = "{ isActive : true}", exists = true)
    Page<Movie> findAllByActive(Pageable pageable);
}

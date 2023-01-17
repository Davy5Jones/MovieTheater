package com.jb.MovieTheater.repos.theater;


import com.jb.MovieTheater.beans.mongo.Theater;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TheaterRepository extends MongoRepository<Theater, String>, TheaterRepositoryTemplate {
    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, String theaterId);

    @Query(value = "{ _id : ?0}", fields = "{rows: 1}")
    Optional<Theater> getTheaterRows(String theaterId);
}

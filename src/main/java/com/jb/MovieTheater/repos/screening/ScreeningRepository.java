package com.jb.MovieTheater.repos.screening;

import com.jb.MovieTheater.beans.mongo.Screening;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.Instant;
import java.util.Optional;

public interface ScreeningRepository extends MongoRepository<Screening, String>, ScreeningRepositoryTemplate {

    @Query(value = "{ _id : ?0}", fields = "{ screenTime: 1,movieId: 1,theater: 1,duration: 1,movieName : 1}")
    Optional<Screening> getScreeningTimeStampAndMovieIdAndTheaterAndDuration(String screeningId);


    @Query(value = "{screenTime:{ $gt: ?0, $lt: ?1 },isActive:true}")
    Page<Screening> findScreeningsByScreenTimeBetweenAndActive(Instant after, Instant before, Pageable pageable);

    boolean existsByTheaterIdAndScreenTimeBetween(String theaterId, Instant before, Instant after);

    @Query(value = "{ _id : ?0, isActive : true}", exists = true)
    boolean getScreeningActive(String screeningId);

    @Query(value = "{isActive : true}")
    Page<Screening> findAllByActive(Pageable pageable);

    @Query(value = "{isActive : true,movieId: ?0}")
    Page<Screening> findAllByActiveAndMovieId(String movieName, Pageable pageable);

    @Query(value = "{ movieId: ?0, isActive : true}", exists = true)
    boolean existsByMovieIdAndActive(String movieId);

    Page<Screening> findAllByMovieId(String movieId,Pageable pageable);
}

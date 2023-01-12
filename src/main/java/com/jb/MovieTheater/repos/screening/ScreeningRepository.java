package com.jb.MovieTheater.repos.screening;

import com.fasterxml.jackson.databind.util.ArrayBuilders;
import com.jb.MovieTheater.beans.mongo.Screening;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ScreeningRepository extends MongoRepository<Screening, String> , ScreeningRepositoryTemplate {

    @Query(value = "{ _id : ?0}", fields = "{ screenTime: 1,movieId: 1,theater: 1,duration: 1}")
    Optional<Screening> getScreeningTimeStampAndMovieIdAndTheaterAndDuration(String screeningId);


    @Query(value = "{screenTime:{ $gt: ?0, $lt: ?1 },isActive:true}")
    List<Screening> findScreeningsByScreenTimeBetweenAndActive(Instant after,Instant before);

    boolean existsByTheaterIdAndScreenTimeBetween(String theaterId, Instant before, Instant after);

    boolean existsByTheaterIdAndScreenTimeBetweenAndIdNot(String theaterId, Instant before, Instant after, String screeningId);

    @Query(value = "{ _id : ?0}", fields = "{seats: 1}")
    Optional<Screening> findScreeningSeats(String screeningId);

    @Query(value = "{ _id : ?0, isActive : true}", exists = true)
    boolean getScreeningActive(String screeningId);

    List<Screening> findAllByActive(boolean isActive, Pageable pageable);
    @Query(value = "{ movieName : ?0, isActive : true}", exists = true)
    boolean existsByMovieNameAndActive(String movieName);



}

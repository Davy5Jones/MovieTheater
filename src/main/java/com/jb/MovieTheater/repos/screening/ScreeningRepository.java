package com.jb.MovieTheater.repos.screening;

import com.jb.MovieTheater.beans.mongo.Screening;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface ScreeningRepository extends MongoRepository<Screening, String>, ScreeningRepositoryTemplate {

    boolean existsByTheaterIdAndScreenTimeBetween(String theaterId, Instant before, Instant after);

    @Query(value = "{ _id : ?0, isActive : true}", exists = true)
    boolean getScreeningActive(String screeningId);

    @Query(value = "{isActive : true}")
    Page<Screening> findAllByActive(Pageable pageable);

    @Query(value = "{isActive : true,movieId: ?0}")
    Page<Screening> findAllByActiveAndMovieId(String movieId, Pageable pageable);

    @Query(value = "{ movieId: ?0, isActive : true}", exists = true)
    boolean existsByMovieIdAndActive(String movieId);

    Page<Screening> findAllByMovieId(String movieId, Pageable pageable);
}

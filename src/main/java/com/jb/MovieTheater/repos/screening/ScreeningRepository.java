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

    @Query(value = "{ _id : ?0, active : true}", exists = true)
    boolean getScreeningActive(String screeningId);

    @Query(value = "{active : true}")
    Page<Screening> findAllByActive(Pageable pageable);

    @Query(value = "{active : true,movieId: ?0}")
    Page<Screening> findAllByActiveAndMovieId(String movieId, Pageable pageable);

    @Query(value = "{ movieId: ?0, active : true}", exists = true)
    boolean existsByMovieIdAndActive(String movieId);

    @Query(value = "{ theaterId: ?0, active : true}", exists = true)
    boolean existsByTheaterAndActive(String theaterId);
    @Query(value = "{ movieId: ?0, active : true}")
    Page<Screening> findAllByMovieIdAndActive(String movieId, Pageable pageable);
}

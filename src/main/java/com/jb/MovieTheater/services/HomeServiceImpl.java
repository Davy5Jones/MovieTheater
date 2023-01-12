package com.jb.MovieTheater.services;

import com.jb.MovieTheater.beans.mongo.Movie;
import com.jb.MovieTheater.beans.mongo.Screening;
import com.jb.MovieTheater.models.screening.ScreeningModelDto;
import com.jb.MovieTheater.repos.movie.MovieRepository;
import com.jb.MovieTheater.repos.screening.ScreeningRepository;
import com.jb.MovieTheater.repos.theater.TheaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;
@RequiredArgsConstructor
@Service
public class HomeServiceImpl implements HomeService{
    private final MovieRepository movieRepository;
    private final ScreeningRepository screeningRepository;
    private final TheaterRepository theaterRepository;



    @Override
    @Cacheable(value = "recommendedMovies")
    public List<Movie> getRecommendedMovies(){
        return movieRepository.findTop5ByIsActiveOrderByRating(true);
    }

    @Override
    //@Cacheable(value = "dailyScreenings", cacheNames = "dailyScreenings")
    public List<ScreeningModelDto> todayScreeningsByTime(int page, int size) {
        List<Screening> screenings = screeningRepository
                .findScreeningsByScreenTimeBetweenAndActive(Instant.now(), Instant.ofEpochMilli(LocalDate.now().plusDays(1)
                        .atStartOfDay().toEpochSecond(ZoneOffset.ofHours(0))));

        return screenings.stream().map(screening ->
                new ScreeningModelDto(screening.getId(), screening.getMovieName(), screening.getScreenTime(),  theaterRepository.getTheaterName(screening.getTheaterId()).getName(), screening.is3D())).collect(Collectors.toList());
    }
}

package com.jb.MovieTheater.assemblers;

import com.jb.MovieTheater.beans.mongo.Movie;
import com.jb.MovieTheater.models.movie.MovieModelDto;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class MovieModelAssembler extends RepresentationModelAssemblerSupport<Movie, MovieModelDto> {


    public MovieModelAssembler() {
        super(Movie.class, MovieModelDto.class);
    }

    @Override
    public MovieModelDto toModel(Movie movie) {
        return MovieModelDto.builder()
                .id(movie.getId())
                .rating(movie.getRating())
                .category(movie.getCategory())
                .description(movie.getDescription())
                .duration(movie.getDuration())
                .name(movie.getName())
                .isActive(movie.isActive())
                .build();
    }
}

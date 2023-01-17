package com.jb.MovieTheater.assemblers;

import com.jb.MovieTheater.beans.mongo.Screening;
import com.jb.MovieTheater.models.screening.ScreeningModelDto;
import com.jb.MovieTheater.repos.theater.TheaterRepository;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class ScreeningModelAssembler extends RepresentationModelAssemblerSupport<Screening, ScreeningModelDto> {
    private final TheaterRepository theaterRepository;

    public ScreeningModelAssembler(TheaterRepository theaterRepository) {
        super(Screening.class, ScreeningModelDto.class);
        this.theaterRepository = theaterRepository;
    }

    @Override
    public ScreeningModelDto toModel(Screening screening) {
        return new ScreeningModelDto(screening.getId(),
                screening.getMovieId(), screening.getMovieName(),
                screening.getScreenTime(), screening.getSeats(), theaterRepository.getTheaterNameById(screening.getTheaterId()), screening.is3D(), screening.isActive());

    }
}

package com.jb.MovieTheater.assemblers;

import com.jb.MovieTheater.beans.mongo.Theater;
import com.jb.MovieTheater.models.theater.TheaterModelDto;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class TheaterModelAssembler extends RepresentationModelAssemblerSupport<Theater, TheaterModelDto> {
    public TheaterModelAssembler() {
        super(Theater.class, TheaterModelDto.class);
    }

    @Override
    public TheaterModelDto toModel(Theater theater) {
        return new TheaterModelDto(theater.getId(), theater.getName(), theater.getRows());
    }
}

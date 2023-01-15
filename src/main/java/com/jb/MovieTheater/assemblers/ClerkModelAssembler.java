package com.jb.MovieTheater.assemblers;

import com.jb.MovieTheater.beans.mysql.Clerk;
import com.jb.MovieTheater.models.user.ClerkModelDto;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class ClerkModelAssembler extends RepresentationModelAssemblerSupport<Clerk, ClerkModelDto> {
    public ClerkModelAssembler() {
        super(Clerk.class, ClerkModelDto.class);
    }

    @Override
    public ClerkModelDto toModel(Clerk clerk) {
        return new ClerkModelDto(clerk.getId(), clerk.getEmail(), clerk.getName());
    }
}

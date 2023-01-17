package com.jb.MovieTheater.models.screening;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScreeningModelDao {
    @NotBlank(message = "Movie name cannot be empty!")
    private String movieName;
    @NotNull(message = "screening must contain screening time")
    private Instant screenTime;
    @NotBlank(message = "must contain theaterId")
    private String theaterId;
    private boolean is3D;
}

package com.jb.MovieTheater.models.screening;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;


@Data@AllArgsConstructor
@NoArgsConstructor
public class ScreeningModelDao {
    @NotBlank(message = "Movie Id cannot be empty!")
    private String movieId;
    @NotNull(message = "screening must contain screening time")
    private Instant screenTime;
    @NotBlank(message = "must contain theater name")
    private String theaterName;
    private boolean threeD;
    private boolean active;
}

package com.jb.MovieTheater.models.theater;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TheaterModelDao {
    @NotBlank(message = "Theater name can't be blank")
    private String name;
    @NotNull(message = "theater must have rows")
    private List<Integer> rows;
}

package com.jb.MovieTheater.models.movie;

import com.jb.MovieTheater.beans.mongo.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MovieModelDao {
    @NotBlank(message = "movie name cannot be blank!")
    @Length(max = 40, message = "name must be below 40 letters")
    private String name;
    @Length(max = 250, message = "description must contain up to 250 letters")
    @NotBlank(message = "description cannot be empty!")
    private String description;
    @Min(value = 40, message = "duration must be above 40 minutes")
    @Max(value = 170, message = "maximum duration is 170 minutes")
    private int duration;
    @NotNull(message = "category cannot be empty...")
    private Category category;
    @Min(value = 1, message = "rating cannot be negative...")
    @Max(value = 5, message = "rating maximum is 5")
    private float rating;

}

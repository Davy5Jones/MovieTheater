package com.jb.MovieTheater.models.movie;

import com.jb.MovieTheater.beans.mongo.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MovieModelDao {
    @NotBlank
    @Length(max = 40,message = "name must be below 40 letters")
    private String name;
    private String description;
    @Min(value = 40,message = "duration must be above 40 minutes")
    @Max(value = 140,message = "maximum duration is 140 minutes")
    private int duration;
    private Category category;
    @Min(value = 0,message = "rating cannot be negative...")
    @Max(value = 5,message = "rating maximum is 5")
    private float rating;

}

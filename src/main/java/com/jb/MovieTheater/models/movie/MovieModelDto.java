package com.jb.MovieTheater.models.movie;

import com.jb.MovieTheater.beans.SuperBean;
import com.jb.MovieTheater.beans.mongo.Category;
import com.jb.MovieTheater.beans.mongo.Movie;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.validation.constraints.NotBlank;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieModelDto extends SuperBean {
    @MongoId
    private String id;
    @NotBlank
    @Length(max = 40)
    private String name;
    private String description;
    private int duration;
    private Category category;
    private float rating;
    private boolean isActive;

    public MovieModelDto(Movie movie) {
        this.id = movie.getId();
        this.name = movie.getName();
        this.description = movie.getDescription();
        this.duration = movie.getDuration();
        this.category = movie.getCategory();
        this.rating = movie.getRating();
        this.isActive = movie.isActive();
    }
}

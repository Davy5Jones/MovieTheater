package com.jb.MovieTheater.beans.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;


@Document("movies")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Movie {
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

    public Movie(String name, String description, int duration, Category category, float rating,boolean isActive) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.category = category;
        this.rating = rating;
        this.isActive =isActive;
    }
}

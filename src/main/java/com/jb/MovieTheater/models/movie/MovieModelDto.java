package com.jb.MovieTheater.models.movie;

import com.jb.MovieTheater.beans.SuperBean;
import com.jb.MovieTheater.beans.mongo.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieModelDto extends SuperBean {
    private String id;
    private String name;
    private String description;
    private int duration;
    private Category category;
    private float rating;
    private boolean isActive;
    private String img;
    private String trailer;
}

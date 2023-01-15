package com.jb.MovieTheater.models.theater;

import com.jb.MovieTheater.beans.SuperBean;
import com.jb.MovieTheater.beans.mongo.Theater;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TheaterModelDto extends SuperBean {
    private String id;
    private String name;
    private List<Integer> rows = new ArrayList<>();

}

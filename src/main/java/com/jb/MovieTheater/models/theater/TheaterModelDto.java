package com.jb.MovieTheater.models.theater;

import com.jb.MovieTheater.beans.SuperBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

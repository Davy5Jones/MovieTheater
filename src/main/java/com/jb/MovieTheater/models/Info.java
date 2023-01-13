package com.jb.MovieTheater.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Info {
    private int pages;
    private int current;
    private long count;
}

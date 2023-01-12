package com.jb.MovieTheater.repos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageableModel <T>{
    private int totalPages;
    private List<T> results;
}

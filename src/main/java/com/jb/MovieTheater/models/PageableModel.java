package com.jb.MovieTheater.models;

import com.jb.MovieTheater.beans.SuperBean;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PageableModel<T> extends SuperBean {
    private Info info;
    private List<T> results;

    public PageableModel(int totalPages, long count, int current, List<T> results) {
        this.results = results;
        this.info = new Info(totalPages, current, count);
    }
}

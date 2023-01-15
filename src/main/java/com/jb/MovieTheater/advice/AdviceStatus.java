package com.jb.MovieTheater.advice;

import lombok.Data;

@Data
public class AdviceStatus {
    private final String key = "151 Key";
    private String message;

    public AdviceStatus(String message) {
        this.message = message;
    }
}

package com.jb.MovieTheater.exception;

public class CustomCinemaException extends Exception {

    public CustomCinemaException(CinemaExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMessage());
    }
}

package com.jb.MovieTheater.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CinemaExceptionEnum {
    SEAT_IS_TAKEN("seat is already taken"),
    SCREENING_DOESNT_EXIST("Screening doesn't exist"),
    SEAT_DOESNT_EXIST("Seat doesn't exist"),
    MOVIE_DOESNT_EXIST("Movie doesn't exist"),
    SCREENING_IS_INACTIVE("Screening has already occurred"),
    NO_THEATER_ROWS("Theater must have rows documentation"),
    EMAIL_IN_USE("Email is already in use!"), NAME_ALREADY_INUSE("Name field is already in use!"), THEATER_DOESNT_EXIST("Theater doesn't exist"),
    THEATER_IN_USE("There must be at least 2.5 hours between screenings in theater"),
    USER_NOT_FOUND("Couldn't find user"), MOVIE_IS_INACTIVE("Movie is inactive"),
    INVALID_SCREENING_DATE("Cannot add same day screenings"),
    MOVIE_HAS_ACTIVE_SCREENINGS("movie still has active screenings!"),
    CANNOT_UPDATE_MOVIE_NAME("Movie name is not updatable"), PURCHASE_DOESNT_EXIST("purchase doesnt exist!"),
    PURCHASE_OWNED_BY_ANOTHER_CUSTOMER("Cannot request ticket of another customer!");

    private final String message;
}

package com.jb.MovieTheater.models.screening;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jb.MovieTheater.beans.mongo.Movie;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.Instant;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScreeningModelDao {
    private String movieName;
    private Instant screenTime;
    private String theaterId;
    private boolean is3D;
}

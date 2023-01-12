package com.jb.MovieTheater.models.screening;

import com.jb.MovieTheater.beans.mongo.Movie;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScreeningModelDto {

    private String id;
    private String movieName;
    private LocalDateTime screenTime;
    private List<boolean[]> seats;
    private String theaterName;
    private boolean is3D;

    public ScreeningModelDto(String id, String movieName, Instant screenTime, String theaterName, boolean is3D) {
        this.id = id;
        this.movieName = movieName;
        this.screenTime = LocalDateTime.ofInstant(screenTime, ZoneId.of("Asia/Tel_Aviv"));
        this.theaterName = theaterName;
        this.is3D = is3D;
    }
}

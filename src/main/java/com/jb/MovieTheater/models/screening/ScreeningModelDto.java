package com.jb.MovieTheater.models.screening;

import com.jb.MovieTheater.beans.SuperBean;
import com.jb.MovieTheater.beans.mongo.Screening;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScreeningModelDto extends SuperBean {

    private String id;
    private String movieId;
    private String movieName;
    private Instant screenTime;
    private List<boolean[]> seats;
    private String theaterName;
    private boolean is3D;
    private boolean isActive;

    public ScreeningModelDto(String id, String movieId, String movieName, Instant screenTime, String theaterName, boolean is3D, boolean isActive) {
        this.id = id;
        this.movieId = movieId;
        this.movieName = movieName;
        this.screenTime = screenTime;
        this.theaterName = theaterName;
        this.is3D = is3D;
        this.isActive = isActive;
    }

}

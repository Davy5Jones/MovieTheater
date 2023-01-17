package com.jb.MovieTheater.models.screening;

import com.jb.MovieTheater.beans.SuperBean;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
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


}

package com.jb.MovieTheater.models.ticket;

import com.jb.MovieTheater.beans.SuperBean;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;


@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class TicketModelDto extends SuperBean {
    private String id;
    private Instant dateTime;
    private int duration;
    private String theaterName;
    private String userEmail;
    private String movieName;
    private int rowId;
    private int seatId;
    private boolean isUsed;
}

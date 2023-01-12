package com.jb.MovieTheater.models.ticket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;


@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class TicketModelDto {
    private Instant dateTime;
    private int duration;
    private String theaterName;
    private String userEmail;
    private String movieName;
    private int rowId;
    private int seatId;
    private boolean isUsed;
}

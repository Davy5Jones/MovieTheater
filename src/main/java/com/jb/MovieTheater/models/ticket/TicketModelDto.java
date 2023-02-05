package com.jb.MovieTheater.models.ticket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.Instant;


@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class TicketModelDto extends RepresentationModel<TicketModelDto> {
    private String id;
    private Instant dateTime;
    private Instant purchaseTime;
    private int duration;
    private String theaterName;
    private int userId;
    private String userEmail;
    private String movieName;
    private int rowId;
    private int seatId;
    private boolean isUsed;
}

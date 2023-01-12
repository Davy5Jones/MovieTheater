package com.jb.MovieTheater.models.ticket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketModelDao {
    private String screeningId;
    private int rowId;
    private int seatId;
}

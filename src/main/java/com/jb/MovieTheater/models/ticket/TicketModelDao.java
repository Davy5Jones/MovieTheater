package com.jb.MovieTheater.models.ticket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketModelDao {
    @NotBlank(message = "must contain screeningId")
    private String screeningId;
    @Min(value = 0, message = "row number cannot be negative")
    private int rowId;
    @Min(value = 0, message = "seat number cannot be negative")
    private int seatId;
}

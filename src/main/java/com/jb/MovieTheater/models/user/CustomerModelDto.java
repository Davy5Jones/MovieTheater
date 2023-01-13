package com.jb.MovieTheater.models.user;

import com.jb.MovieTheater.beans.SuperBean;
import com.jb.MovieTheater.models.ticket.TicketModelDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerModelDto extends SuperBean {
    private int id;
    private String emailAddress;
    private String customerName;
    private List<TicketModelDto> customerTickets = new ArrayList<>();
}

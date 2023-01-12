package com.jb.MovieTheater.services;

import com.jb.MovieTheater.beans.mongo.Movie;
import com.jb.MovieTheater.exception.CustomCinemaException;
import com.jb.MovieTheater.models.screening.ScreeningModelDto;
import com.jb.MovieTheater.models.ticket.TicketModelDao;
import com.jb.MovieTheater.models.ticket.TicketModelDto;
import com.jb.MovieTheater.models.user.CustomerModelDao;
import com.jb.MovieTheater.models.user.CustomerModelDto;

import java.time.Instant;
import java.util.List;

public interface CustomerService {

    TicketModelDto purchaseTicket(TicketModelDao ticket, int userId) throws CustomCinemaException;

    List<TicketModelDto> findAllUserTickets(int userId);

    CustomerModelDto register(CustomerModelDao customerModel) throws CustomCinemaException;

    CustomerModelDto getCustomerDetails(int customerId) throws CustomCinemaException;

}

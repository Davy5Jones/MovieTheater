package com.jb.MovieTheater.services;

import com.jb.MovieTheater.exception.CustomCinemaException;
import com.jb.MovieTheater.models.screening.ScreeningModelDto;
import com.jb.MovieTheater.models.ticket.TicketModelDto;
import com.jb.MovieTheater.models.user.ClerkModelDto;

import java.util.List;

public interface ClerkService {

    TicketModelDto invalidatePurchase(String purchaseId);

    List<TicketModelDto> findCustomerTicketsByEmail(String email);

    List<ScreeningModelDto> todayScreenings(int page, int pageSize);



    List<ScreeningModelDto> getActiveScreeningsPage(int page, int size);

    ClerkModelDto getClerkDetails(int clerkId) throws CustomCinemaException;
}

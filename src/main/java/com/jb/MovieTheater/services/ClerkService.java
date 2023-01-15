package com.jb.MovieTheater.services;

import com.jb.MovieTheater.exception.CustomCinemaException;
import com.jb.MovieTheater.models.movie.MovieModelDto;
import com.jb.MovieTheater.models.screening.ScreeningModelDto;
import com.jb.MovieTheater.models.ticket.TicketModelDto;
import com.jb.MovieTheater.models.user.ClerkModelDto;
import org.springframework.data.domain.Page;

public interface ClerkService {

    TicketModelDto invalidatePurchase(String purchaseId);


    Page<TicketModelDto> findCustomerTicketsPageByEmail(String email, int page,int pageSize, String sortBy);

    TicketModelDto findSingleUserTicket(String purchaseId) throws CustomCinemaException;

    Page<ScreeningModelDto> todayScreenings(int page,int pageSize, String sortBy);


    Page<ScreeningModelDto> getActiveScreenings(int page,int pageSize, String sortBy);

    ClerkModelDto getClerkDetails(int clerkId) throws CustomCinemaException;

    Page<MovieModelDto> getActiveMoviesPage(int page,int pageSize, String sortBy);
}

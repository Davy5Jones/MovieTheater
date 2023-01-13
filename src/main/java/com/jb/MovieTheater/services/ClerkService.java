package com.jb.MovieTheater.services;

import com.jb.MovieTheater.beans.mongo.Movie;
import com.jb.MovieTheater.exception.CustomCinemaException;
import com.jb.MovieTheater.models.PageableModel;
import com.jb.MovieTheater.models.screening.ScreeningModelDto;
import com.jb.MovieTheater.models.ticket.TicketModelDto;
import com.jb.MovieTheater.models.user.ClerkModelDto;

import java.util.List;

public interface ClerkService {

    TicketModelDto invalidatePurchase(String purchaseId);

    List<TicketModelDto> findCustomerTicketsByEmail(String email);

    PageableModel<ScreeningModelDto> todayScreenings(int page, String sortBy);


    PageableModel<ScreeningModelDto> getActiveScreenings(int page, String sortBy);

    ClerkModelDto getClerkDetails(int clerkId) throws CustomCinemaException;

    PageableModel<Movie> getActiveMoviesPage(int page, String sortBy);
}

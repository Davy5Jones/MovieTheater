package com.jb.MovieTheater.services;

import com.jb.MovieTheater.beans.mongo.Purchase;
import com.jb.MovieTheater.beans.mysql.Clerk;
import com.jb.MovieTheater.exception.CustomCinemaException;
import org.springframework.data.domain.Page;

public interface ClerkService {

    Purchase invalidatePurchase(String purchaseId) throws CustomCinemaException;


    Page<Purchase> findCustomerTicketsPageByEmail(String email, int page, int pageSize, String sortBy) throws CustomCinemaException;

    Purchase findSingleUserTicket(String purchaseId) throws CustomCinemaException;

    Clerk getClerkDetails(int clerkId) throws CustomCinemaException;

}

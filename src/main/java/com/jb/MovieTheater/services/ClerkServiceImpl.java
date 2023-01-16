package com.jb.MovieTheater.services;

import com.jb.MovieTheater.beans.mongo.Purchase;
import com.jb.MovieTheater.beans.mysql.Clerk;
import com.jb.MovieTheater.exception.CinemaExceptionEnum;
import com.jb.MovieTheater.exception.CustomCinemaException;
import com.jb.MovieTheater.repos.ClerkRepository;
import com.jb.MovieTheater.repos.CustomerRepository;
import com.jb.MovieTheater.repos.movie.MovieRepository;
import com.jb.MovieTheater.repos.purchase.PurchaseRepository;
import com.jb.MovieTheater.repos.screening.ScreeningRepository;
import com.jb.MovieTheater.repos.theater.TheaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ClerkServiceImpl implements ClerkService {
    private final PurchaseRepository purchaseRepository;
    private final ScreeningRepository screeningRepository;
    private final ClerkRepository clerkRepository;
    private final MovieRepository movieRepository;
    private final TheaterRepository theaterRepository;
    private final CustomerRepository customerRepository;

    @Override
    public Purchase invalidatePurchase(String purchaseId) throws CustomCinemaException {
        return purchaseRepository.invalidatePurchase(purchaseId).orElseThrow(() -> new CustomCinemaException(CinemaExceptionEnum.PURCHASE_DOESNT_EXIST));
    }

    @Override
    public Page<Purchase> findCustomerTicketsPageByEmail(String email, int page, int pageSize, String sortBy) throws CustomCinemaException {
        int customerId = customerRepository.getIdByEmail(email).orElseThrow(() -> new CustomCinemaException(CinemaExceptionEnum.USER_NOT_FOUND));
        return purchaseRepository.findAllByUserId(customerId, PageRequest.of(page, pageSize, Sort.by(sortBy)));
    }

    @Override
    public Purchase findSingleUserTicket(String purchaseId) throws CustomCinemaException {
        return purchaseRepository.findById(purchaseId).orElseThrow(() -> new CustomCinemaException(CinemaExceptionEnum.PURCHASE_DOESNT_EXIST));
    }

    @Override
    public Clerk getClerkDetails(int clerkId) throws CustomCinemaException {
        return clerkRepository.findById(clerkId).orElseThrow(() -> new CustomCinemaException(CinemaExceptionEnum.USER_NOT_FOUND));
    }


}

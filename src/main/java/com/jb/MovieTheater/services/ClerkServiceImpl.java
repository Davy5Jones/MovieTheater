package com.jb.MovieTheater.services;

import com.jb.MovieTheater.beans.mongo.Movie;
import com.jb.MovieTheater.beans.mongo.Purchase;
import com.jb.MovieTheater.beans.mongo.Screening;
import com.jb.MovieTheater.beans.mysql.Clerk;
import com.jb.MovieTheater.exception.CinemaExceptionEnum;
import com.jb.MovieTheater.exception.CustomCinemaException;
import com.jb.MovieTheater.models.movie.MovieModelDto;
import com.jb.MovieTheater.models.screening.ScreeningModelDto;
import com.jb.MovieTheater.models.ticket.TicketModelDto;
import com.jb.MovieTheater.models.user.ClerkModelDto;
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

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

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
    public TicketModelDto invalidatePurchase(String purchaseId) {
        Purchase purchase = purchaseRepository.invalidatePurchase(purchaseId);
        Screening screening = screeningRepository.getScreeningTimeStampAndMovieIdAndTheaterAndDuration(purchase.getScreeningId()).orElseThrow();
        int duration = screening.getDuration();
        String email = customerRepository.getEmailById(purchase.getUserId());
        return new TicketModelDto(purchase.getId(), screening.getScreenTime()
                , duration, theaterRepository.getTheaterNameById(screening.getTheaterId())
                , email, screening.getMovieName()
                , purchase.getRowId(), purchase.getSeatId(), purchase.isUsed());
    }

    @Override
    public Page<TicketModelDto> findCustomerTicketsPageByEmail(String email, int page,int pageSize, String sortBy) {
        int customerId = customerRepository.getIdByEmail(email);
        Page<Purchase> pager = purchaseRepository.findAllByUserId(customerId, PageRequest.of(page, pageSize, Sort.by(sortBy)));

        return pager
                .map(purchase -> {
                    Screening screening = screeningRepository.getScreeningTimeStampAndMovieIdAndTheaterAndDuration(purchase.getScreeningId()).orElseThrow();
                    int duration = screening.getDuration();
                    return new TicketModelDto(purchase.getId(), screening.getScreenTime()
                            , duration, theaterRepository.getTheaterNameById(screening.getTheaterId())
                            , email, screening.getMovieName()
                            , purchase.getRowId(), purchase.getSeatId(), purchase.isUsed());
                });
    }

    @Override
    public TicketModelDto findSingleUserTicket(String purchaseId) throws CustomCinemaException {
        Purchase purchase = purchaseRepository.findById(purchaseId).orElseThrow(() -> new CustomCinemaException(CinemaExceptionEnum.PURCHASE_DOESNT_EXIST));
        Screening screening = screeningRepository.getScreeningTimeStampAndMovieIdAndTheaterAndDuration(purchase.getScreeningId()).orElseThrow();
        int duration = screening.getDuration();
        String email = customerRepository.getEmailById(purchase.getUserId());
        return new TicketModelDto(purchase.getId(), screening.getScreenTime()
                , duration, theaterRepository.getTheaterNameById(screening.getTheaterId())
                , email, screening.getMovieName()
                , purchase.getRowId(), purchase.getSeatId(), purchase.isUsed());
    }

    @Override
    public Page<ScreeningModelDto> todayScreenings(int page,int pageSize, String sortBy) {
        Page<Screening> page1 = screeningRepository
                .findScreeningsByScreenTimeBetweenAndActive(Instant.ofEpochMilli(LocalDate.now().atStartOfDay().toEpochSecond(ZoneOffset.ofHours(0))), Instant.ofEpochMilli(LocalDate.now().plusDays(1)
                        .atStartOfDay().toEpochSecond(ZoneOffset.ofHours(0))), PageRequest.of(page, pageSize, Sort.by(sortBy).descending()));

        return page1.map(screening ->
                new ScreeningModelDto(screening.getId(), screening.getMovieId(), screening.getMovieName(), screening.getScreenTime(), theaterRepository.getTheaterNameById(screening.getTheaterId()), screening.is3D(), screening.isActive()));
    }

    @Override
    public Page<ScreeningModelDto> getActiveScreenings(int page,int pageSize, String sortBy) {
        Page<Screening> screeningsPage = screeningRepository.findAllByActive(PageRequest.of(page, pageSize, Sort.by(sortBy).descending()));
        return screeningsPage.map(screening ->
                new ScreeningModelDto(screening.getId(), screening.getMovieId(), screening.getMovieName(), screening.getScreenTime(), theaterRepository.getTheaterNameById(screening.getTheaterId()), screening.is3D(), screening.isActive()));
    }

    @Override
    public Page<MovieModelDto> getActiveMoviesPage(int page,int pageSize, String sortBy) {
        Page<Movie> moviePage = movieRepository.findAllByActive(PageRequest.of(page, pageSize, Sort.by(sortBy)));
        return moviePage.map(MovieModelDto::new);
    }

    @Override
    public ClerkModelDto getClerkDetails(int clerkId) throws CustomCinemaException {
        Clerk clerk = clerkRepository.findById(clerkId).orElseThrow(() -> new CustomCinemaException(CinemaExceptionEnum.USER_NOT_FOUND));
        return new ClerkModelDto(clerk.getId(), clerk.getEmail(), clerk.getName());
    }


}

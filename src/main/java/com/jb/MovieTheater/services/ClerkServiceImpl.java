package com.jb.MovieTheater.services;

import com.jb.MovieTheater.beans.mongo.Purchase;
import com.jb.MovieTheater.beans.mongo.Screening;
import com.jb.MovieTheater.beans.mysql.Clerk;
import com.jb.MovieTheater.exception.CinemaExceptionEnum;
import com.jb.MovieTheater.exception.CustomCinemaException;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

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
        return new TicketModelDto(screening.getScreenTime()
                , duration, theaterRepository.getTheaterName(screening.getTheaterId()).getName()
                , email, screening.getMovieName()
                , purchase.getRowId(), purchase.getSeatId(), purchase.isUsed());
    }
    @Override
    public List<TicketModelDto> findCustomerTicketsByEmail(String email){
        int customerId =customerRepository.getIdByEmail(email);
        return purchaseRepository.findAllByUserId(customerId)
                .stream()
                .map(purchase -> {
                    Screening screening = screeningRepository.getScreeningTimeStampAndMovieIdAndTheaterAndDuration(purchase.getScreeningId()).orElseThrow();
                    int duration = screening.getDuration();
                    return new TicketModelDto(screening.getScreenTime()
                            , duration, theaterRepository.getTheaterName(screening.getTheaterId()).getName()
                            , email, screening.getMovieName()
                            , purchase.getRowId(), purchase.getSeatId(), purchase.isUsed());
                })
                .collect(Collectors.toList());
    }


    @Override
    public List<ScreeningModelDto> todayScreenings(int page, int pageSize) {
        List<Screening> screenings = screeningRepository
                .findScreeningsByScreenTimeBetweenAndActive(Instant.ofEpochMilli(LocalDate.now().atStartOfDay().toEpochSecond(ZoneOffset.ofHours(0))), Instant.ofEpochMilli(LocalDate.now().plusDays(1)
                        .atStartOfDay().toEpochSecond(ZoneOffset.ofHours(0))));

        return screenings.stream().map(screening ->
                new ScreeningModelDto(screening.getId(), screening.getMovieName(), screening.getScreenTime(), theaterRepository.getTheaterName(screening.getTheaterId()).getName(), screening.is3D())).collect(Collectors.toList());
    }

    @Override
    public List<ScreeningModelDto> getActiveScreeningsPage(int page, int size) {
        List<Screening> screenings = screeningRepository.findAllByActive(true, PageRequest.of(page, size));
        return screenings.stream().map(screening ->
                new ScreeningModelDto(screening.getId(), screening.getMovieName(), screening.getScreenTime(), theaterRepository.getTheaterName(screening.getTheaterId()).getName(), screening.is3D())).collect(Collectors.toList());
    }

    @Override
    public ClerkModelDto getClerkDetails(int clerkId) throws CustomCinemaException {
        Clerk clerk = clerkRepository.findById(clerkId).orElseThrow(() -> new CustomCinemaException(CinemaExceptionEnum.USER_NOT_FOUND));
        return new ClerkModelDto(clerk.getId(), clerk.getEmail(), clerk.getName());
    }
}

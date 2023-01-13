package com.jb.MovieTheater.services;

import com.jb.MovieTheater.beans.mongo.Movie;
import com.jb.MovieTheater.beans.mongo.Purchase;
import com.jb.MovieTheater.beans.mongo.Screening;
import com.jb.MovieTheater.beans.mysql.Customer;
import com.jb.MovieTheater.exception.CinemaExceptionEnum;
import com.jb.MovieTheater.exception.CustomCinemaException;
import com.jb.MovieTheater.models.PageableModel;
import com.jb.MovieTheater.models.screening.ScreeningModelDto;
import com.jb.MovieTheater.models.ticket.TicketModelDao;
import com.jb.MovieTheater.models.ticket.TicketModelDto;
import com.jb.MovieTheater.models.user.CustomerModelDto;
import com.jb.MovieTheater.repos.CustomerRepository;
import com.jb.MovieTheater.repos.movie.MovieRepository;
import com.jb.MovieTheater.repos.purchase.PurchaseRepository;
import com.jb.MovieTheater.repos.screening.ScreeningRepository;
import com.jb.MovieTheater.repos.theater.TheaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final PurchaseRepository purchaseRepository;
    private final ScreeningRepository screeningRepository;
    private final CustomerRepository customerRepository;
    private final MovieRepository movieRepository;
    private final TheaterRepository theaterRepository;
    private final int pageSize = 10;


    @Override
    public TicketModelDto purchaseTicket(TicketModelDao ticket, int customerId) throws CustomCinemaException {

        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomCinemaException(CinemaExceptionEnum.USER_NOT_FOUND));

        Screening screening = screeningRepository
                .findById(ticket.getScreeningId())
                .orElseThrow(() -> new CustomCinemaException(CinemaExceptionEnum.SCREENING_DOESNT_EXIST));
        if (!screeningRepository.getScreeningActive(ticket.getScreeningId())) {
            throw new CustomCinemaException(CinemaExceptionEnum.SCREENING_IS_INACTIVE);
        }
        try {
            if (screening.getSeats().get(ticket.getRowId())[ticket.getSeatId()]) {
                throw new CustomCinemaException(CinemaExceptionEnum.SEAT_IS_TAKEN);
            }
        } catch (IndexOutOfBoundsException e) {
            throw new CustomCinemaException(CinemaExceptionEnum.SEAT_DOESNT_EXIST);
        }
        screening.getSeats().get(ticket.getRowId())[ticket.getSeatId()] = true;
        screeningRepository.save(screening);
        Purchase purchase = purchaseRepository.save(new Purchase(customerId, ticket.getScreeningId(), ticket.getRowId(), ticket.getSeatId()));

        return new TicketModelDto(purchase.getId(), Instant.now(), movieRepository.getMovieDurationByName(screening.getMovieName()), theaterRepository.getTheaterNameById(screening.getTheaterId()), customer.getEmail(), screening.getMovieName(), purchase.getRowId(), purchase.getSeatId(), false);
    }

    @Override
    public List<TicketModelDto> findAllUserTickets(int customerId) {
        return purchaseRepository.findAllByUserId(customerId)
                .stream()
                .map(purchase -> {
                    Screening screening = screeningRepository.getScreeningTimeStampAndMovieIdAndTheaterAndDuration(purchase.getScreeningId()).orElseThrow();
                    String email = customerRepository.getEmailById(customerId);
                    return new TicketModelDto(purchase.getId(), screening.getScreenTime()
                            , screening.getDuration(), theaterRepository.getTheaterNameById(screening.getTheaterId())
                            , email, screening.getMovieName()
                            , purchase.getRowId(), purchase.getSeatId(), purchase.isUsed());
                })
                .collect(Collectors.toList());
    }

    @Override
    public TicketModelDto findSingleUserTicket(String purchaseId, int userId) throws CustomCinemaException {
        Purchase purchase = purchaseRepository.findById(purchaseId).orElseThrow(() -> new CustomCinemaException(CinemaExceptionEnum.PURCHASE_DOESNT_EXIST));
        if (purchase.getUserId() != userId) {
            throw new CustomCinemaException(CinemaExceptionEnum.PURCHASE_OWNED_BY_ANOTHER_CUSTOMER);
        }
        Screening screening = screeningRepository.getScreeningTimeStampAndMovieIdAndTheaterAndDuration(purchase.getScreeningId()).orElseThrow();
        int duration = screening.getDuration();
        String email = customerRepository.getEmailById(purchase.getUserId());
        return new TicketModelDto(purchase.getId(), screening.getScreenTime()
                , duration, theaterRepository.getTheaterNameById(screening.getTheaterId())
                , email, screening.getMovieName()
                , purchase.getRowId(), purchase.getSeatId(), purchase.isUsed());
    }


    @Override
    public CustomerModelDto getCustomerDetails(int customerId) throws CustomCinemaException {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomCinemaException(CinemaExceptionEnum.USER_NOT_FOUND));
        List<TicketModelDto> ticketList = purchaseRepository.findAllByUserId(customer.getId())
                .stream()
                .map(purchase -> {
                    Screening screening = screeningRepository.getScreeningTimeStampAndMovieIdAndTheaterAndDuration(purchase.getScreeningId()).orElseThrow();
                    int duration = screening.getDuration();
                    return new TicketModelDto(purchase.getId(), screening.getScreenTime()
                            , duration, theaterRepository.getTheaterNameById(screening.getTheaterId())
                            , customer.getEmail(), screening.getMovieName()
                            , purchase.getRowId(), purchase.getSeatId(), purchase.isUsed());
                })
                .collect(Collectors.toList());
        return new CustomerModelDto(customer.getId(), customer.getEmail(), customer.getName(), ticketList);
    }

    @Override
    public PageableModel<ScreeningModelDto> getActiveScreeningsPage(int page, String sortBy) {
        Page<Screening> screeningsPage = screeningRepository.findAllByActive(PageRequest.of(page, pageSize, Sort.by(sortBy)));
        List<ScreeningModelDto> screenings = screeningsPage.stream().map(screening ->
                new ScreeningModelDto(screening.getId(), screening.getMovieId(), screening.getMovieName(), screening.getScreenTime(), theaterRepository.getTheaterNameById(screening.getTheaterId()), screening.is3D(), screening.isActive())).collect(Collectors.toList());
        return new PageableModel<>(screeningsPage.getTotalPages(), screeningsPage.getTotalPages(), page, screenings);
    }

    @Override
    public ScreeningModelDto getSingleScreening(String screeningId) throws CustomCinemaException {
        Screening screening = screeningRepository.findById(screeningId).orElseThrow(() -> new CustomCinemaException(CinemaExceptionEnum.SCREENING_DOESNT_EXIST));
        if (!screening.isActive()) {
            throw new CustomCinemaException(CinemaExceptionEnum.SCREENING_IS_INACTIVE);
        }
        return new ScreeningModelDto(screening.getId(), screening.getMovieId(), screening.getMovieName(), screening.getScreenTime(), theaterRepository.getTheaterNameById(screening.getTheaterId()), screening.is3D(), screening.isActive());
    }

    @Override
    public PageableModel<ScreeningModelDto> getActiveScreeningsPageByMovie(int page, String movieId, String sortBy) {
        Page<Screening> screeningsPage = screeningRepository
                .findAllByActiveAndMovieId(movieId, PageRequest.of(page, pageSize, Sort.by(sortBy)));
        List<ScreeningModelDto> screenings = screeningsPage.stream().map(screening ->
                new ScreeningModelDto(screening.getId(), screening.getMovieId()
                        , screening.getMovieName(), screening.getScreenTime()
                        , theaterRepository.getTheaterNameById(screening.getTheaterId())
                        , screening.is3D(), screening.isActive())).collect(Collectors.toList());
        return new PageableModel<>(screeningsPage.getTotalPages()
                , screeningsPage.getTotalElements(), page, screenings);
    }

    @Override
    public PageableModel<Movie> getActiveMoviesPage(int page, String sortBy) {
        Page<Movie> moviePage = movieRepository.findAllByActive(PageRequest.of(page, pageSize, Sort.by(sortBy)));
        return new PageableModel<>(moviePage.getTotalPages(), moviePage.getTotalElements(), page, moviePage.getContent());
    }

    @Override
    public Movie getSingleMovie(String movieId) throws CustomCinemaException {
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new CustomCinemaException(CinemaExceptionEnum.MOVIE_DOESNT_EXIST));
        if (!movie.isActive()) {
            throw new CustomCinemaException(CinemaExceptionEnum.MOVIE_IS_INACTIVE);
        }
        return movie;
    }

    @Override
    @Cacheable(value = "recommendedMovies", cacheNames = "recommendedMovies")
    public List<Movie> getRecommendedMovies() {
        return movieRepository.findTop5ByIsActiveOrderByRating(true);
    }

}
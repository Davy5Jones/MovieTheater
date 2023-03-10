package com.jb.MovieTheater.services;

import com.jb.MovieTheater.beans.mongo.Movie;
import com.jb.MovieTheater.beans.mongo.Purchase;
import com.jb.MovieTheater.beans.mongo.Screening;
import com.jb.MovieTheater.beans.mysql.Customer;
import com.jb.MovieTheater.exception.CinemaExceptionEnum;
import com.jb.MovieTheater.exception.CustomCinemaException;
import com.jb.MovieTheater.models.ticket.TicketModelDao;
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
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {


    private final ScreeningRepository screeningRepository;
    private final CustomerRepository customerRepository;
    private final PurchaseRepository purchaseRepository;
    private final TheaterRepository theaterRepository;
    private final MovieRepository movieRepository;


    @Override
    public Purchase purchaseTicket(TicketModelDao ticket, int customerId) throws CustomCinemaException {
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
        String email = customerRepository.getEmailById(customerId).orElseThrow(()-> new CustomCinemaException(CinemaExceptionEnum.USER_NOT_FOUND));
        return purchaseRepository.save(new Purchase(email,customerId, ticket.getScreeningId(), Instant.now(), ticket.getRowId(), ticket.getSeatId(), false));
    }

    @Override
    public Page<Purchase> findAllUserTickets(int page, int pageSize, int customerId) {
        return purchaseRepository.findAllByUserId(customerId, PageRequest.of(page, pageSize));

    }

   @Override
    public Purchase findSingleUserTicket(String purchaseId, int userId) throws CustomCinemaException {
        Purchase purchase = purchaseRepository.findById(purchaseId).orElseThrow(() -> new CustomCinemaException(CinemaExceptionEnum.PURCHASE_DOESNT_EXIST));
        String email = customerRepository.getEmailById(userId).orElseThrow(()-> new CustomCinemaException(CinemaExceptionEnum.USER_NOT_FOUND));
        if (!purchase.getUserEmail().equals(email)) {
            throw new CustomCinemaException(CinemaExceptionEnum.PURCHASE_OWNED_BY_ANOTHER_CUSTOMER);
        }
        return purchase;
    }


    @Override
    public Customer getCustomerDetails(int customerId) throws CustomCinemaException {
        return customerRepository.findById(customerId).orElseThrow(() -> new CustomCinemaException(CinemaExceptionEnum.USER_NOT_FOUND));
    }

    @Override
    public Page<Screening> getActiveScreeningsPage(int page, int pageSize, String sortBy) {

        return screeningRepository.findAllByActive(PageRequest.of(page, pageSize, Sort.by(sortBy)));
    }

    @Override
    public Screening getSingleScreening(String screeningId) throws CustomCinemaException {
        Screening screening = screeningRepository.findById(screeningId).orElseThrow(() -> new CustomCinemaException(CinemaExceptionEnum.SCREENING_DOESNT_EXIST));
        if (!screening.isActive()) {
            throw new CustomCinemaException(CinemaExceptionEnum.SCREENING_IS_INACTIVE);
        }
        return screening;
    }

    @Override
    public Page<Screening> getActiveScreeningsPageByMovie(int page, int size, String movieId, String sort) {
        return screeningRepository
                .findAllByActiveAndMovieId(movieId, PageRequest.of(page, size, Sort.by(sort)));
    }

    @Override
    //@Cacheable(value = "movies", cacheNames = "movies",condition = "#page<3&&#pageSize==5&&#sort=='name'")
    public Page<Movie> getActiveMoviesPage(int page, int pageSize, String sort) {
        Page<Movie> allByActive = movieRepository.findAllByActive(PageRequest.of(page, pageSize, Sort.by(sort)));
        System.out.println(allByActive.getSort());
        return
                allByActive;
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
        return movieRepository.findTop5ByIsActiveOrderByRatingDesc(true);
    }

}
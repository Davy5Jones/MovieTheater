package com.jb.MovieTheater.services;

import com.jb.MovieTheater.beans.mongo.Movie;
import com.jb.MovieTheater.beans.mongo.Purchase;
import com.jb.MovieTheater.beans.mongo.Screening;
import com.jb.MovieTheater.beans.mongo.Theater;
import com.jb.MovieTheater.beans.mysql.Clerk;
import com.jb.MovieTheater.beans.mysql.Customer;
import com.jb.MovieTheater.exception.CinemaExceptionEnum;
import com.jb.MovieTheater.exception.CustomCinemaException;
import com.jb.MovieTheater.models.movie.MovieModelDao;
import com.jb.MovieTheater.models.screening.ScreeningModelDao;
import com.jb.MovieTheater.models.theater.TheaterModelDao;
import com.jb.MovieTheater.models.user.ClerkModelDao;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AdminServiceImpl implements AdminService {
    private final MovieRepository movieRepository;
    private final TheaterRepository theaterRepository;

    private final ScreeningRepository screeningRepository;
    private final ClerkRepository clerkRepository;
    private final CustomerRepository customerRepository;
    private final PurchaseRepository purchaseRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public Theater addTheater(TheaterModelDao theater) throws CustomCinemaException {
        if (theaterRepository.existsByName(theater.getName())) {
            throw new CustomCinemaException(CinemaExceptionEnum.NAME_ALREADY_INUSE);
        }
        if (theater.getRows().size() == 0) {
            throw new CustomCinemaException(CinemaExceptionEnum.NO_THEATER_ROWS);
        }
        return theaterRepository
                .save(new Theater(theater.getName(), theater.getRows()));
    }

    @Override
    public Movie addMovie(MovieModelDao movie) throws CustomCinemaException {
        if (movieRepository.existsByName(movie.getName())) {
            throw new CustomCinemaException(CinemaExceptionEnum.NAME_ALREADY_INUSE);
        }
        return movieRepository.save(new Movie(movie.getName(), movie.getDescription(), movie.getDuration(), movie.getCategory(), movie.getRating(), true, movie.getImg(), movie.getTrailer()));
    }

    @Override
    public Screening addScreening(ScreeningModelDao screening) throws CustomCinemaException {
        if (!movieRepository.existsById(screening.getMovieId())) {
            throw new CustomCinemaException(CinemaExceptionEnum.MOVIE_DOESNT_EXIST);
        }
        if (!movieRepository.getMovieIsActiveById(screening.getMovieId())) {
            throw new CustomCinemaException(CinemaExceptionEnum.MOVIE_IS_INACTIVE);
        }
        Instant time = screening.getScreenTime();
        Movie movie = movieRepository.getMovieDurationAndNameById(screening.getMovieId());
        int minutes = movie.getDuration();
        if (time.isBefore(Instant.now())) {
            throw new CustomCinemaException(CinemaExceptionEnum.INVALID_SCREENING_DATE);
        }
        Instant start = time.minusSeconds(60 * 150);
        Instant end = time.plusSeconds(minutes * 60L + 60 * 10);
        Theater theater = theaterRepository.getTheaterRowsAndIdByName(screening.getTheaterName()).orElseThrow(() -> new CustomCinemaException(CinemaExceptionEnum.THEATER_DOESNT_EXIST));
        List<boolean[]> rows = theater.getRows()
                .stream().map(boolean[]::new).collect(Collectors.toList());
        if (screeningRepository.existsByTheaterIdAndScreenTimeBetween(theater.getId(), start, end)) {
            throw new CustomCinemaException(CinemaExceptionEnum.THEATER_IN_USE);
        }

        return screeningRepository.save(Screening.builder()
                .duration(minutes)
                .movieId(screening.getMovieId())
                .movieName(movie.getName())
                .seats(rows)
                .screenTime(screening.getScreenTime())
                .theaterId(theater.getId())
                .is3D(screening.isThreeD())
                .active(screening.isActive())
                .build());
    }

    @Override
    public Clerk addClerk(ClerkModelDao clerk) throws CustomCinemaException {
        if (clerkRepository.existsByEmail(clerk.getEmailAddress()))
            throw new CustomCinemaException(CinemaExceptionEnum.EMAIL_IN_USE);
        return clerkRepository.save(Clerk.builder()
                .email(clerk.getEmailAddress())
                .password(bCryptPasswordEncoder.encode(clerk.getPassword()))
                .name(clerk.getClerkName())
                .build());
    }

    @Override
    public Theater updateTheater(TheaterModelDao theater, String theaterId) throws CustomCinemaException {
        if (!theaterRepository.existsById(theaterId)) {
            throw new CustomCinemaException(CinemaExceptionEnum.THEATER_DOESNT_EXIST);
        }
        if (theaterRepository.existsByNameAndIdNot(theater.getName(), theaterId)) {
            throw new CustomCinemaException(CinemaExceptionEnum.NAME_ALREADY_INUSE);
        }
        if (screeningRepository.existsByTheaterAndActive(theaterId)) {
            throw new CustomCinemaException(CinemaExceptionEnum.THEATER_HAS_ACTIVE_SCREENINGS);
        }

        return theaterRepository.save(Theater.builder()
                .name(theater.getName())
                .rows(theater.getRows())
                .build());
    }

    @Override
    public Movie updateMovie(MovieModelDao movie, String movieId) throws CustomCinemaException {
        if (!movieRepository.existsById(movieId)) {
            throw new CustomCinemaException(CinemaExceptionEnum.MOVIE_DOESNT_EXIST);
        }
        if (!movieRepository.existsByNameAndId(movie.getName(), movieId)) {
            throw new CustomCinemaException(CinemaExceptionEnum.CANNOT_UPDATE_MOVIE_NAME);
        }
        screeningRepository.updateScreeningsDurationByMovieId(movieId, movie.getDuration());
        return movieRepository.updateMovie(movie, movieId);
    }


    @Override
    public Clerk updateClerk(ClerkModelDao clerk, int clerkId) throws CustomCinemaException {
        if (!clerkRepository.existsById(clerkId)) {
            throw new CustomCinemaException(CinemaExceptionEnum.USER_NOT_FOUND);
        }
        if (clerkRepository.existsByEmailAndIdNot(clerk.getEmailAddress(), clerkId)) {
            throw new CustomCinemaException(CinemaExceptionEnum.EMAIL_IN_USE);
        }
        return clerkRepository.save(Clerk.builder()
                .password(clerk.getPassword())
                .email(clerk.getEmailAddress())
                .id(clerkId)
                .name(clerk.getClerkName())
                .build());
    }

    @Override
    public Movie inactivateMovie(String movieId) throws CustomCinemaException {
        if (screeningRepository.existsByMovieIdAndActive(movieId)) {
            throw new CustomCinemaException(CinemaExceptionEnum.MOVIE_HAS_ACTIVE_SCREENINGS);
        }
        return movieRepository.inactivateMovie(movieId).orElseThrow(() -> new CustomCinemaException(CinemaExceptionEnum.MOVIE_DOESNT_EXIST));
    }

    @Override
    public Screening inactivateScreening(String screeningId) throws CustomCinemaException {
        return screeningRepository.inactivateScreening(screeningId).orElseThrow(() -> new CustomCinemaException(CinemaExceptionEnum.SCREENING_DOESNT_EXIST));
    }

    @Override
    public void deleteClerk(int clerkId) throws CustomCinemaException {
        if (!clerkRepository.existsById(clerkId)) {
            throw new CustomCinemaException(CinemaExceptionEnum.USER_NOT_FOUND);
        }
        clerkRepository.deleteById(clerkId);
    }

    @Override
    public Page<Customer> getCustomerPage(int page, int pageSize, String sortBy, Sort.Direction order) {
        return customerRepository.findAll(PageRequest.of(page, pageSize, Sort.by(order, sortBy)));
    }

    @Override
    public Customer getSingleCustomer(int customerId) throws CustomCinemaException {
        return customerRepository.findById(customerId).orElseThrow(() -> new CustomCinemaException(CinemaExceptionEnum.USER_NOT_FOUND));
    }

    @Override
    public Page<Clerk> getClerksPage(int page, int pageSize, String sortBy, Sort.Direction order) {
        return clerkRepository.findAll(PageRequest.of(page, pageSize, Sort.by(sortBy)));
    }

    @Override
    public Clerk getSingleClerk(int clerkId) throws CustomCinemaException {

        return clerkRepository.findById(clerkId).orElseThrow(() -> new CustomCinemaException(CinemaExceptionEnum.USER_NOT_FOUND));
    }

    @Override
    public Page<Movie> getMoviePage(int page, int pageSize, String sortBy, Sort.Direction order) {
        return movieRepository.findAll(PageRequest.of(page, pageSize, Sort.by(order, sortBy)));
    }

    @Override
    public Page<Screening> getActiveScreeningsPageByMovie(int page, int pageSize, String movieId, String sortBy, Sort.Direction order) {

        return screeningRepository.findAllByMovieIdAndActive(movieId, PageRequest.of(page, pageSize, Sort.by(order, sortBy)));
    }

    @Override
    public Movie getSingleMovie(String movieId) throws CustomCinemaException {
        return movieRepository.findById(movieId).orElseThrow(() -> new CustomCinemaException(CinemaExceptionEnum.MOVIE_DOESNT_EXIST));
    }

    @Override
    public Page<Screening> getScreeningPage(int page, int pageSize, String sortBy, Sort.Direction order) {
        System.out.println(order);
        return screeningRepository
                .findAll(PageRequest.of(page, pageSize, Sort.by(order, sortBy)));
    }

    @Override
    public Screening getSingleScreening(String screeningId) throws CustomCinemaException {
        return screeningRepository.findById(screeningId)
                .orElseThrow(() -> new CustomCinemaException(CinemaExceptionEnum.SCREENING_DOESNT_EXIST));
    }

    @Override
    public Page<Purchase> getPurchasePage(int page, int pageSize, String sortBy, Sort.Direction order) {
        return purchaseRepository.findAll(PageRequest.of(page, pageSize, Sort.by(order, sortBy)));
    }

    @Override
    public Page<Purchase> getPurchasePageByCustomer(int customerId, int page, int pageSize, String sortBy, Sort.Direction order) {
        return purchaseRepository.findAllByUserId(customerId, PageRequest.of(page, pageSize, Sort.by(order, sortBy)));
    }

    @Override
    public Purchase getSinglePurchase(String purchaseId) throws CustomCinemaException {
        return purchaseRepository.findById(purchaseId).orElseThrow(() -> new CustomCinemaException(CinemaExceptionEnum.PURCHASE_DOESNT_EXIST));
    }

    @Override
    public List<String> getTheaterNames() {
        return theaterRepository.getAllNames().stream().map(Theater::getName).collect(Collectors.toList());
    }

}

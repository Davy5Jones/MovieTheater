package com.jb.MovieTheater.services;

import com.jb.MovieTheater.beans.mongo.Movie;
import com.jb.MovieTheater.beans.mongo.Screening;
import com.jb.MovieTheater.beans.mongo.Theater;
import com.jb.MovieTheater.beans.mysql.Clerk;
import com.jb.MovieTheater.beans.mysql.Customer;
import com.jb.MovieTheater.exception.CinemaExceptionEnum;
import com.jb.MovieTheater.exception.CustomCinemaException;
import com.jb.MovieTheater.models.PageableModel;
import com.jb.MovieTheater.models.movie.MovieModelDao;
import com.jb.MovieTheater.models.screening.ScreeningModelDao;
import com.jb.MovieTheater.models.screening.ScreeningModelDto;
import com.jb.MovieTheater.models.theater.TheaterModelDao;
import com.jb.MovieTheater.models.ticket.TicketModelDto;
import com.jb.MovieTheater.models.user.ClerkModelDao;
import com.jb.MovieTheater.models.user.ClerkModelDto;
import com.jb.MovieTheater.models.user.CustomerModelDto;
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
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AdminServiceImpl implements AdminService {
    private final TheaterRepository theaterRepository;
    private final MovieRepository movieRepository;
    private final ScreeningRepository screeningRepository;
    private final ClerkRepository clerkRepository;
    private final CustomerRepository customerRepository;
    private final PurchaseRepository purchaseRepository;

    private final int pageSize = 16;

    @Override
    public Theater addTheater(TheaterModelDao theater) throws CustomCinemaException {
        if (theaterRepository.existsByName(theater.getName())) {
            throw new CustomCinemaException(CinemaExceptionEnum.NAME_ALREADY_INUSE);
        }
        if (theater.getRows().size() == 0) {
            throw new CustomCinemaException(CinemaExceptionEnum.NO_THEATER_ROWS);
        }
        return theaterRepository.save(new Theater(theater.getName(), theater.getRows()));
    }

    @Override
    public Movie addMovie(MovieModelDao movie) throws CustomCinemaException {
        if (movieRepository.existsByName(movie.getName())) {
            throw new CustomCinemaException(CinemaExceptionEnum.NAME_ALREADY_INUSE);
        }
        return movieRepository.save(new Movie(movie.getName(), movie.getDescription(), movie.getDuration(), movie.getCategory(), movie.getRating(), true));
    }

    @Override
    public Screening addScreening(ScreeningModelDao screening) throws CustomCinemaException {
        if (!movieRepository.existsByName(screening.getMovieName())) {
            throw new CustomCinemaException(CinemaExceptionEnum.MOVIE_DOESNT_EXIST);
        }
        if (!movieRepository.getMovieIsActiveByName(screening.getMovieName())) {
            throw new CustomCinemaException(CinemaExceptionEnum.MOVIE_IS_INACTIVE);
        }
        Instant time = screening.getScreenTime();
        int minutes = movieRepository.getMovieDurationByName(screening.getMovieName());
        if (time.isBefore(Instant.now())) {
            throw new CustomCinemaException(CinemaExceptionEnum.INVALID_SCREENING_DATE);
        }
        Instant start = time.minusSeconds(60 * 150);
        Instant end = time.plusSeconds(minutes * 60L + 60 * 10);
        List<boolean[]> rows = theaterRepository.getTheaterRows(screening.getTheaterId())
                .orElseThrow(() -> new CustomCinemaException(CinemaExceptionEnum.THEATER_DOESNT_EXIST)).getRows()
                .stream().map(boolean[]::new).collect(Collectors.toList());
        if (screeningRepository.existsByTheaterIdAndScreenTimeBetween(screening.getTheaterId(), start, end)) {
            throw new CustomCinemaException(CinemaExceptionEnum.THEATER_IN_USE);
        }

        return screeningRepository.save(Screening.builder()
                .duration(minutes)
                .movieName(screening.getMovieName())
                .seats(rows)
                .screenTime(screening.getScreenTime())
                .theaterId(screening.getTheaterId())
                .is3D(screening.is3D())
                .isActive(true)
                .build());
    }

    @Override
    public ClerkModelDto addClerk(ClerkModelDao clerk) throws CustomCinemaException {
        if (clerkRepository.existsByEmail(clerk.getEmailAddress()))
            throw new CustomCinemaException(CinemaExceptionEnum.EMAIL_IN_USE);
        Clerk clerkdb = clerkRepository.save(Clerk.builder()
                .email(clerk.getEmailAddress())
                .password(clerk.getPassword())
                .name(clerk.getClerkName())
                .build());
        return new ClerkModelDto(clerkdb.getId(), clerk.getEmailAddress(), clerk.getClerkName());
    }

    @Override
    public Theater updateTheater(TheaterModelDao theater, String theaterId) throws CustomCinemaException {
        if (!theaterRepository.existsById(theaterId)) {
            throw new CustomCinemaException(CinemaExceptionEnum.THEATER_DOESNT_EXIST);
        }
        if (theaterRepository.existsByNameAndIdNot(theater.getName(), theaterId)) {
            throw new CustomCinemaException(CinemaExceptionEnum.NAME_ALREADY_INUSE);
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
    public ClerkModelDto updateClerk(ClerkModelDao clerk, int clerkId) throws CustomCinemaException {
        if (!clerkRepository.existsById(clerkId)) {
            throw new CustomCinemaException(CinemaExceptionEnum.USER_NOT_FOUND);
        }
        if (clerkRepository.existsByEmailAndIdNot(clerk.getEmailAddress(), clerkId)) {
            throw new CustomCinemaException(CinemaExceptionEnum.EMAIL_IN_USE);
        }
        Clerk clerk1 = clerkRepository.save(Clerk.builder()
                .password(clerk.getPassword())
                .email(clerk.getEmailAddress())
                .id(clerkId)
                .name(clerk.getClerkName())
                .build());
        return new ClerkModelDto(clerk1.getId(), clerk1.getEmail(), clerk1.getName());
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
    public PageableModel<CustomerModelDto> getCustomerPage(int page, String sortBy) {

        Page<Customer> page1 = customerRepository.findAll(PageRequest.of(page, pageSize, Sort.by(sortBy)));
        int count = page1.getTotalPages();
        List<CustomerModelDto> customerList = page1.stream().map(customer -> new CustomerModelDto(customer.getId(), customer.getEmail(), customer.getName()
                , purchaseRepository.findAllByUserId(customer.getId())
                .stream()
                .map(purchase -> {
                    Screening screening = screeningRepository.getScreeningTimeStampAndMovieIdAndTheaterAndDuration(purchase.getScreeningId()).orElseThrow();
                    int duration = screening.getDuration();
                    String movieName = screening.getMovieName();
                    String email = customerRepository.getEmailById(customer.getId());
                    return new TicketModelDto(purchase.getId(), screening.getScreenTime()
                            , duration, theaterRepository.getTheaterNameById(screening.getTheaterId())
                            , email, movieName
                            , purchase.getRowId(), purchase.getSeatId(), purchase.isUsed());
                })
                .collect(Collectors.toList()))).collect(Collectors.toList());
        return new PageableModel<>(count, page1.getTotalElements(), page, customerList);
    }

    @Override
    public PageableModel<ClerkModelDto> getClerksPage(int page, String sortBy) {
        Page<Clerk> clerks = clerkRepository.findAll(PageRequest.of(page, pageSize, Sort.by(sortBy)));
        int pages = clerks
                .getTotalPages();
        List<ClerkModelDto> clerkModelDtos = clerks.map(clerk -> new ClerkModelDto(clerk.getId(), clerk.getEmail(), clerk.getName())).toList();
        return new PageableModel<>(pages, clerks.getTotalElements(), page, clerkModelDtos);
    }

    @Override
    public ClerkModelDto getSingleClerk(int clerkId) throws CustomCinemaException {
        Clerk clerk = clerkRepository.findById(clerkId).orElseThrow(() -> new CustomCinemaException(CinemaExceptionEnum.USER_NOT_FOUND));

        return new ClerkModelDto(clerk.getId(), clerk.getEmail(), clerk.getName());
    }

    @Override
    public PageableModel<Movie> getMoviePage(int page, String sortBy) {
        Page<Movie> page1 = movieRepository.findAll(PageRequest.of(page, pageSize, Sort.by(sortBy)));
        List<Movie> movies = page1.toList();
        int pages = page1.getTotalPages();
        return new PageableModel<>(pages, page1.getTotalElements(), page, movies);
    }

    @Override
    public Movie getSingleMovie(String movieId) throws CustomCinemaException {
        return movieRepository.findById(movieId).orElseThrow(() -> new CustomCinemaException(CinemaExceptionEnum.MOVIE_DOESNT_EXIST));
    }

    @Override
    public PageableModel<ScreeningModelDto> getScreeningPage(int page, String sortBy) {
        Page<Screening> screeningPage = screeningRepository
                .findAll(PageRequest.of(page, pageSize, Sort.by(sortBy).descending()));
        List<ScreeningModelDto> screenings = screeningPage.stream().map(screening -> new ScreeningModelDto(screening.getId(), screening.getMovieId(), screening.getMovieName(), screening.getScreenTime(), theaterRepository
                .getTheaterNameById(screening.getTheaterId()), screening.is3D(), screening.isActive())).collect(Collectors.toList());
        return new PageableModel<>(screeningPage.getTotalPages(), screeningPage.getTotalElements(), page, screenings);
    }

    @Override
    public ScreeningModelDto getSingleScreening(String screeningId) throws CustomCinemaException {
        Screening screening = screeningRepository.findById(screeningId).orElseThrow(() -> new CustomCinemaException(CinemaExceptionEnum.SCREENING_DOESNT_EXIST));
        return new ScreeningModelDto(screening.getId(), screening.getMovieId(), screening.getMovieName(), screening.getScreenTime(), theaterRepository.getTheaterNameById(screening.getTheaterId()), screening.is3D(), screening.isActive());
    }


}

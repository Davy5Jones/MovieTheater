package com.jb.MovieTheater.services;

import com.jb.MovieTheater.beans.mongo.Movie;
import com.jb.MovieTheater.beans.mongo.Screening;
import com.jb.MovieTheater.beans.mongo.Theater;
import com.jb.MovieTheater.beans.mysql.Clerk;
import com.jb.MovieTheater.exception.CinemaExceptionEnum;
import com.jb.MovieTheater.exception.CustomCinemaException;
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
import com.jb.MovieTheater.repos.purchase.PurchaseRepository;
import com.jb.MovieTheater.repos.movie.MovieRepository;
import com.jb.MovieTheater.repos.screening.ScreeningRepository;
import com.jb.MovieTheater.repos.theater.TheaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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

    private final int pageSize=16;

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
        return movieRepository.save(new Movie(movie.getName(),movie.getDescription(),movie.getDuration(),movie.getCategory(),movie.getRating(),true));
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
        Clerk clerkdb= clerkRepository.save(Clerk.builder()
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
        screeningRepository.updateScreeningsDurationByMovieName(movie.getName(), movie.getDuration());
        return movieRepository.updateMovie(movie,movieId);
    }



    @Override
    public ClerkModelDto updateClerk(ClerkModelDao clerk, int clerkId) throws CustomCinemaException {
        if (!clerkRepository.existsById(clerkId)) {
            throw new CustomCinemaException(CinemaExceptionEnum.USER_NOT_FOUND);
        }
        if (clerkRepository.existsByEmailAndIdNot(clerk.getEmailAddress(), clerkId)) {
            throw new CustomCinemaException(CinemaExceptionEnum.EMAIL_IN_USE);

        }
        Clerk clerk1= clerkRepository.save(Clerk.builder()
                .password(clerk.getPassword())
                .email(clerk.getEmailAddress())
                .id(clerkId)
                        .name(clerk.getClerkName())
                .build());
        return new ClerkModelDto(clerk1.getId(),clerk1.getEmail(),clerk1.getName());
    }

    @Override
    public void inactivateMovie(String movieId) throws CustomCinemaException {
        if (screeningRepository.existsByMovieNameAndActive(movieRepository.getMovieName(movieId))){
            throw new CustomCinemaException(CinemaExceptionEnum.MOVIE_HAS_ACTIVE_SCREENINGS);
        }
       movieRepository.inactivateMovie(movieId);
    }

    @Override
    public Screening inactivateScreening(String screeningId) throws CustomCinemaException {
        Screening screening =screeningRepository.inactivateScreening(screeningId);
        if (screening==null){
            throw new CustomCinemaException(CinemaExceptionEnum.SCREENING_DOESNT_EXIST);
        }
        return screening;
    }

    @Override
    public void deleteClerk(int clerkId) throws CustomCinemaException {
        if (!clerkRepository.existsById(clerkId)) {
            throw new CustomCinemaException(CinemaExceptionEnum.USER_NOT_FOUND);
        }
        clerkRepository.deleteById(clerkId);
    }
    @Override
    public List<CustomerModelDto> getCustomerPage(int page){
        return customerRepository.findAll(PageRequest.of(page, pageSize)).stream().map(customer -> new CustomerModelDto(customer.getId(), customer.getEmail(), customer.getName(),purchaseRepository.findAllByUserId(customer.getId())
                .stream()
                .map(purchase -> {
                    Screening screening = screeningRepository.getScreeningTimeStampAndMovieIdAndTheaterAndDuration(purchase.getScreeningId()).orElseThrow();
                    int duration =screening.getDuration();
                    String movieName = screening.getMovieName();
                    String email = customerRepository.getEmailById(customer.getId());
                    return new TicketModelDto(screening.getScreenTime()
                            , duration, theaterRepository.getTheaterName(screening.getTheaterId()).getName()
                            , email, movieName
                            , purchase.getRowId(), purchase.getSeatId(), purchase.isUsed());
                })
                .collect(Collectors.toList()))).collect(Collectors.toList());
    }

    @Override
    public List<ClerkModelDto> getClerksPage(int page) {
        return clerkRepository.findAll(PageRequest.of(page, pageSize)).map(clerk -> new ClerkModelDto(clerk.getId(), clerk.getEmail(), clerk.getName())).toList();
    }

    @Override
    public List<Movie> getMoviePage(int page){
        return movieRepository.findAll(PageRequest.of(page, pageSize)).toList();
    }
    @Override
    public List<ScreeningModelDto> getScreeningPage(int page){
        return screeningRepository.findAll(PageRequest.of(page, pageSize)).stream().map(screening -> new ScreeningModelDto()).collect(Collectors.toList());
    }


}

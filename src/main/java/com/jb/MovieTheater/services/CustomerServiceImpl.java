package com.jb.MovieTheater.services;

import com.jb.MovieTheater.beans.mongo.Movie;
import com.jb.MovieTheater.beans.mongo.Purchase;
import com.jb.MovieTheater.beans.mongo.Screening;
import com.jb.MovieTheater.beans.mysql.Customer;
import com.jb.MovieTheater.exception.CinemaExceptionEnum;
import com.jb.MovieTheater.exception.CustomCinemaException;
import com.jb.MovieTheater.models.screening.ScreeningModelDto;
import com.jb.MovieTheater.models.ticket.TicketModelDao;
import com.jb.MovieTheater.models.ticket.TicketModelDto;
import com.jb.MovieTheater.models.user.CustomerModelDao;
import com.jb.MovieTheater.models.user.CustomerModelDto;
import com.jb.MovieTheater.repos.CustomerRepository;
import com.jb.MovieTheater.repos.purchase.PurchaseRepository;
import com.jb.MovieTheater.repos.theater.TheaterRepository;
import com.jb.MovieTheater.repos.movie.MovieRepository;
import com.jb.MovieTheater.repos.screening.ScreeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
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


    @Override
    public TicketModelDto purchaseTicket(TicketModelDao ticket, int customerId) throws CustomCinemaException {

        Customer customer = customerRepository.findById(customerId).orElseThrow(()-> new CustomCinemaException(CinemaExceptionEnum.USER_NOT_FOUND));

        Screening screening = screeningRepository
                .findById(ticket.getScreeningId())
                .orElseThrow(() -> new CustomCinemaException(CinemaExceptionEnum.SCREENING_DOESNT_EXIST));
        if (!screeningRepository.getScreeningActive(ticket.getScreeningId())) {
            throw new CustomCinemaException(CinemaExceptionEnum.SCREENING_IS_INACTIVE);
        }
        if (screening.getScreenTime().isBefore(Instant.now().minusSeconds(60 * 60))) {
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

        return new TicketModelDto(Instant.now(),movieRepository.getMovieDurationByName(screening.getMovieName()),theaterRepository.getTheaterName(screening.getTheaterId()).getName(), customer.getEmail(), screening.getMovieName(), purchase.getRowId(), purchase.getSeatId(),false);
    }

    @Override
    public List<TicketModelDto> findAllUserTickets(int customerId) {
        return purchaseRepository.findAllByUserId(customerId)
                .stream()
                .map(purchase -> {
                    Screening screening = screeningRepository.getScreeningTimeStampAndMovieIdAndTheater(purchase.getScreeningId()).orElseThrow();
                    String email = customerRepository.getEmailById(customerId);
                    return new TicketModelDto(screening.getScreenTime()
                            , screening.getDuration(), theaterRepository.getTheaterName(screening.getTheaterId()).getName()
                            , email, screening.getMovieName()
                            , purchase.getRowId(), purchase.getSeatId(), purchase.isUsed());
                })
                .collect(Collectors.toList());
    }


    @Override
    public CustomerModelDto register(CustomerModelDao customerModel) throws CustomCinemaException {
        if (customerRepository.existsByEmail(customerModel.getEmailAddress())) {
            throw new CustomCinemaException(CinemaExceptionEnum.EMAIL_IN_USE);
        }
        Customer customer = customerRepository.save(Customer.builder()
                .password(customerModel.getPassword())
                .email(customerModel.getEmailAddress())
                .name(customerModel.getCustomerName())
                .build());
        return new CustomerModelDto(customer.getId(), customer.getEmail(), customer.getName(), new ArrayList<>());
    }

    @Override
    public CustomerModelDto getCustomerDetails(int customerId) throws CustomCinemaException {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomCinemaException(CinemaExceptionEnum.USER_NOT_FOUND));
        List<TicketModelDto> ticketList = purchaseRepository.findAllByUserId(customer.getId())
                .stream()
                .map(purchase -> {
                    Screening screening = screeningRepository.getScreeningTimeStampAndMovieIdAndTheater(purchase.getScreeningId()).orElseThrow();
                    int duration = movieRepository.getMovieDurationByName(screening.getMovieName());
                    return new TicketModelDto(screening.getScreenTime()
                            , duration, theaterRepository.getTheaterName(screening.getTheaterId()).getName()
                            , customer.getEmail(), screening.getMovieName()
                            , purchase.getRowId(), purchase.getSeatId(), purchase.isUsed());
                })
                .collect(Collectors.toList());
        return new CustomerModelDto(customer.getId(), customer.getEmail(), customer.getName(), ticketList);
    }


}
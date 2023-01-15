package com.jb.MovieTheater.assemblers;

import com.jb.MovieTheater.beans.mongo.Purchase;
import com.jb.MovieTheater.beans.mongo.Screening;
import com.jb.MovieTheater.exception.CinemaExceptionEnum;
import com.jb.MovieTheater.exception.CustomCinemaException;
import com.jb.MovieTheater.models.ticket.TicketModelDto;
import com.jb.MovieTheater.repos.CustomerRepository;
import com.jb.MovieTheater.repos.screening.ScreeningRepository;
import com.jb.MovieTheater.repos.theater.TheaterRepository;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class TicketModelAssembler extends RepresentationModelAssemblerSupport<Purchase, TicketModelDto> {
    private final ScreeningRepository screeningRepository;
    private final CustomerRepository customerRepository;
    private final TheaterRepository theaterRepository;
    public TicketModelAssembler(ScreeningRepository screeningRepository, CustomerRepository customerRepository, TheaterRepository theaterRepository) {
        super(Purchase.class, TicketModelDto.class);
        this.screeningRepository = screeningRepository;
        this.customerRepository = customerRepository;
        this.theaterRepository = theaterRepository;
    }

    @Override
    public TicketModelDto toModel(Purchase purchase) {
        Screening screening = screeningRepository.findById(purchase.getScreeningId()).get();
        int duration = screening.getDuration();
        String email = customerRepository.getEmailById(purchase.getUserId());
        return new TicketModelDto(purchase.getId(), screening.getScreenTime()
                , duration, theaterRepository.getTheaterNameById(screening.getTheaterId())
                , email, screening.getMovieName()
                , purchase.getRowId(), purchase.getSeatId(), purchase.isUsed());
    }
}

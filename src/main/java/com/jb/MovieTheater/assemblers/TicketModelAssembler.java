package com.jb.MovieTheater.assemblers;

import com.jb.MovieTheater.beans.mongo.Purchase;
import com.jb.MovieTheater.beans.mongo.Screening;
import com.jb.MovieTheater.models.ticket.TicketModelDto;
import com.jb.MovieTheater.repos.CustomerRepository;
import com.jb.MovieTheater.repos.screening.ScreeningRepository;
import com.jb.MovieTheater.repos.theater.TheaterRepository;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component

//todo maybe add screening id to add link
public class TicketModelAssembler extends RepresentationModelAssemblerSupport<Purchase, TicketModelDto> {
    private final ScreeningRepository screeningRepository;
    private final TheaterRepository theaterRepository;

    public TicketModelAssembler(ScreeningRepository screeningRepository, CustomerRepository customerRepository, TheaterRepository theaterRepository) {
        super(Purchase.class, TicketModelDto.class);
        this.screeningRepository = screeningRepository;
        this.theaterRepository = theaterRepository;
    }

    @Override
    public TicketModelDto toModel(Purchase purchase) {
        Screening screening = screeningRepository.findById(purchase.getScreeningId()).get();
        int duration = screening.getDuration();
        return new TicketModelDto(purchase.getId(), screening.getScreenTime(), purchase.getPurchaseTime()
                , duration, theaterRepository.getTheaterNameById(screening.getTheaterId()),purchase.getUserId()
                , purchase.getUserEmail(), screening.getMovieName()
                , purchase.getRowId(), purchase.getSeatId(), purchase.isUsed());
    }
}

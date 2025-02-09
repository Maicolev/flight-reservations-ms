package com.example.reservation.service;

import com.example.common.dto.ReservationRequest;
import com.example.common.model.Seat;
import com.example.reservation.repository.SeatRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PublishReservationImpl implements PublishReservation {

    private final ValidatorService validatorService;
    private final SeatRepository seatRepository;
    private final RabbitTemplate rabbitTemplate;

    public PublishReservationImpl(RabbitTemplate rabbitTemplate, ValidatorService validatorService, SeatRepository seatRepository){
        this.rabbitTemplate = rabbitTemplate;
        this.validatorService = validatorService;
        this.seatRepository = seatRepository;

        // Asegurar el convertidor
        this.rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
    }

    @Override
    @Transactional
    public void publishReservation(ReservationRequest reservationRequest) {
        Seat seat = validatorService.isValid(reservationRequest);
        if(seat != null) {
            rabbitTemplate.convertAndSend("reservation.exchange", "reservation.pending", reservationRequest);
            seat.setPending(true);
            seatRepository.save(seat);
        }
        else{
            System.out.println("invalid");
            rabbitTemplate.convertAndSend( "reservations.errors", reservationRequest);
        }
    }
}
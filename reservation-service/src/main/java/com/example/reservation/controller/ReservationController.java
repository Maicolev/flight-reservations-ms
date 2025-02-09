package com.example.reservation.controller;

import com.example.common.dto.ReservationRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

// reservation-service/src/main/java/com/example/reservation/controller/ReservationController.java
@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final RabbitTemplate rabbitTemplate;

    public ReservationController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        // Asegurar el convertidor
        this.rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
    }
    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Create reservation request")
    public void createReservation(@Valid @RequestBody ReservationRequest request) {
        System.out.println("entry");
        System.out.println(request);
        // Validación básica
//        if (!isValidSeatFormat(request.seatNumber())) {
//            throw new InvalidSeatException("Formato de asiento inválido");
//        }

       // rabbitTemplate.convertAndSend("reservations.pending", request);
        rabbitTemplate.convertAndSend("reservation.exchange", "reservation.pending", request);
    }

    private boolean isValidSeatFormat(String seat) {
        return seat.matches("[A-Z][0-9]+");
    }
}
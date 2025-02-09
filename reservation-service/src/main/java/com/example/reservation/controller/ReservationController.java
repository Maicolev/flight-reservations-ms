package com.example.reservation.controller;

import com.example.common.dto.ReservationRequest;
import com.example.reservation.service.PublishReservation;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

// reservation-service/src/main/java/com/example/reservation/controller/ReservationController.java
@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final PublishReservation publishReservation;

    public ReservationController(PublishReservation publishReservation) {
        this.publishReservation = publishReservation;
    }
    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Create reservation request")
    public void createReservation(@Valid @RequestBody ReservationRequest request) {
        System.out.println("entry");
        System.out.println(request);

        publishReservation.publishReservation(request);
       // rabbitTemplate.convertAndSend("reservations.pending", request);

    }
}
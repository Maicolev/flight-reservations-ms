package com.example.reservation.controller;

import com.example.common.dto.ReservationRequest;
import com.example.reservation.service.PublishReservation;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final PublishReservation publishReservation;

    public ReservationController(PublishReservation publishReservation) {
        this.publishReservation = publishReservation;
    }

    @PostMapping
    @Operation(summary = "Create reservation request")
    public ResponseEntity<String> createReservation(@Valid @RequestBody ReservationRequest request) {
        System.out.println("Entry");
        System.out.println(request);

        boolean response = publishReservation.publishReservation(request);

        if (response) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Reservation successful, data processed correctly.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("The reservation could not be processed. Review the data sent or availability of the chair.");
        }
    }
}
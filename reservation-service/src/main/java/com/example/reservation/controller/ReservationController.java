package com.example.reservation.controller;

import com.example.common.dto.ReservationRequest;
import com.example.reservation.service.PublishReservation;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @Operation(summary = "Create reservation request")
    public ResponseEntity<String> createReservation(@Valid @RequestBody ReservationRequest request) {
        System.out.println("Entry");
        System.out.println(request);

        boolean response = publishReservation.publishReservation(request);

        // Si la respuesta es exitosa
        if (response) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Reserva exitosa, datos procesados correctamente.");
        } else {
            // Si la respuesta es falsa, se indica que revise los datos
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("La reserva no pudo ser procesada. Revise los datos enviados o disponibilidad de la silla.");
        }
    }
}
package com.example.processing.controller;

import com.example.common.dto.ReservationRequest;
import com.example.common.model.Reservation;
import com.example.processing.service.ReservationProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/processing")
@RequiredArgsConstructor
class ReservationProcessingController {
    private ReservationProcessor processor;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void processReservations(ReservationRequest request) {
        processor.processPendingReservations(request);
    }


    @GetMapping("/confirmed")
    public List<Reservation> getConfirmedReservations(@RequestParam Long flightId) {
        return processor.getConfirmedReservations(flightId);
        //return reservationRepository.findBySeatFlightIdAndStatus(flightId, Reservation.ReservationStatus.CONFIRMED);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void cancelReservation(@PathVariable Long id) {
        processor.cancelReservation(id);
    }
}

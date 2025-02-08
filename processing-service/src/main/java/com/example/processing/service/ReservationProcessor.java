package com.example.processing.service;

import com.example.common.dto.ReservationRequest;
import com.example.common.model.Reservation;

import java.util.List;

public interface ReservationProcessor {
    void processPendingReservations(ReservationRequest reservationRequest);

    List<Reservation> getConfirmedReservations(Long flightId);

    void cancelReservation(Long id);
}
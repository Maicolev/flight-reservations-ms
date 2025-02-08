package com.example.processing.service;

import com.example.common.model.Reservation;

import java.util.List;

public interface ReservationQuery {
    List<Reservation> getConfirmedReservations(Long flightId);
}
package com.example.reservation.service;

import com.example.common.dto.ReservationRequest;

public interface PublishReservation {
    boolean publishReservation(ReservationRequest reservationRequest);
}

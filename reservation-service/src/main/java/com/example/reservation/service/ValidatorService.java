package com.example.reservation.service;

import com.example.common.dto.ReservationRequest;
import com.example.common.model.Seat;

public interface ValidatorService {

    Seat isValid(ReservationRequest reservationRequest);
}

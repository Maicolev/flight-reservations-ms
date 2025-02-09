package com.example.reservation.service;

import com.example.common.dto.ReservationRequest;
import com.example.common.exceptions.FlightNotFoundException;
import com.example.common.exceptions.InvalidSeatException;
import com.example.common.exceptions.SeatNotFoundException;
import com.example.common.model.Seat;
import com.example.reservation.repository.FlighRepository;
import com.example.reservation.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidatorServiceImpl implements ValidatorService {

    private final SeatRepository seatRepository;

    private final FlighRepository flighRepository;

    @Override
    public Seat isValid(ReservationRequest reservationRequest) {
        if(isValidFlight(reservationRequest)){
            return isValidSeat(reservationRequest);
        }
        return null;
    }

    public boolean isValidFlight(ReservationRequest reservationRequest) {
       flighRepository.findById(reservationRequest.flightId())
                .orElseThrow(() -> new FlightNotFoundException("Vuelo no encontrado"));
        return true;
    }

    public Seat isValidSeat(ReservationRequest reservationRequest) {
        if (isValidFormat(reservationRequest)) {
            Seat seat = seatRepository.findByFlightIdAndSeatNumber(reservationRequest.flightId(), reservationRequest.seatNumber())
                    .orElseThrow(() -> new SeatNotFoundException("Asiento no encontrado"));

            if (seat.isReserved()) {
                throw new InvalidSeatException("Asiento reservado");
            }

            if (seat.isPending()) {
                throw new InvalidSeatException("Asiento en transacción");
            }
            return seat;
        }
        return null;
    }

    public boolean isValidFormat(ReservationRequest reservationRequest) {
        return isValidSeatFormat(reservationRequest.seatNumber()) && isValidEmailFormat(reservationRequest.email());
    }

    // Validate seat format: letter followed by number
    public boolean isValidSeatFormat(String seat) {
        return seat.matches("[0-9]+[A-Z]");
    }

    // Validate email format
    public boolean isValidEmailFormat(String email) {
        return email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
}
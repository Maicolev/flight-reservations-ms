package com.example.reservation.service;

import com.example.common.dto.ReservationRequest;
import com.example.common.exceptions.FlightNotFoundException;
import com.example.common.exceptions.InvalidSeatException;
import com.example.common.exceptions.SeatNotFoundException;
import com.example.common.model.Flight;
import com.example.common.model.Seat;
import com.example.reservation.repository.FlighRepository;
import com.example.reservation.repository.SeatRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidatorServiceTest {

    @Mock
    private FlighRepository flighRepository;

    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private ValidatorServiceImpl validatorService;

    private final ReservationRequest validRequest = new ReservationRequest(1L, "12A", "test@example.com");
    private final ReservationRequest invalidSeatRequest = new ReservationRequest(1L, "A12", "test@example.com");
    private final ReservationRequest invalidEmailRequest = new ReservationRequest(1L, "12A", "invalid-email");

    @Test
    void isValid_ValidRequest() {
        when(flighRepository.findById(anyLong())).thenReturn(Optional.of(new Flight()));
        when(seatRepository.findByFlightIdAndSeatNumber(anyLong(), anyString()))
                .thenReturn(Optional.of(new Seat()));

        Seat result = validatorService.isValid(validRequest);

        assertNotNull(result);
    }

    @Test
    void isValid_FlightNotFound() {
        when(flighRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(FlightNotFoundException.class, () -> validatorService.isValid(validRequest));
    }

    @Test
    void isValid_SeatNotFound() {
        when(flighRepository.findById(anyLong())).thenReturn(Optional.of(new Flight()));
        when(seatRepository.findByFlightIdAndSeatNumber(anyLong(), anyString())).thenReturn(Optional.empty());

        assertThrows(SeatNotFoundException.class, () -> validatorService.isValid(validRequest));
    }

    @Test
    void isValid_SeatReserved() {
        Seat reservedSeat = new Seat();
        reservedSeat.setReserved(true);

        when(flighRepository.findById(anyLong())).thenReturn(Optional.of(new Flight()));
        when(seatRepository.findByFlightIdAndSeatNumber(anyLong(), anyString())).thenReturn(Optional.of(reservedSeat));

        assertThrows(InvalidSeatException.class, () -> validatorService.isValid(validRequest));
    }

    @Test
    void isValid_InvalidSeatFormat() {
        when(flighRepository.findById(anyLong())).thenReturn(Optional.of(new Flight()));

        Seat result = validatorService.isValid(invalidSeatRequest);

        assertNull(result);
    }

    @Test
    void isValid_InvalidEmailFormat() {
        when(flighRepository.findById(anyLong())).thenReturn(Optional.of(new Flight()));

        Seat result = validatorService.isValid(invalidEmailRequest);

        assertNull(result);
    }
}
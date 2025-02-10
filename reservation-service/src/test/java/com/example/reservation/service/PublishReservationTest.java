package com.example.reservation.service;

import com.example.common.dto.ReservationRequest;
import com.example.common.exceptions.FlightNotFoundException;
import com.example.common.exceptions.InvalidSeatException;
import com.example.common.model.Seat;
import com.example.reservation.repository.SeatRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PublishReservationTest {

    @Mock
    private ValidatorService validatorService;

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private PublishReservationImpl publishReservation;

    private final ReservationRequest validRequest = new ReservationRequest(1L, "12A", "test@example.com");

    @Test
    void publishReservation_Success() {
        Seat seat = new Seat();
        when(validatorService.isValid(validRequest)).thenReturn(seat);

        boolean result = publishReservation.publishReservation(validRequest);

        assertTrue(result);
        verify(rabbitTemplate).convertAndSend(eq("reservation.exchange"), eq("reservation.pending"), eq(validRequest));
        verify(seatRepository).save(seat);
    }

    @Test
    void publishReservation_InvalidData() {
        when(validatorService.isValid(validRequest)).thenReturn(null);

        boolean result = publishReservation.publishReservation(validRequest);

        assertFalse(result);
        verify(rabbitTemplate).convertAndSend(eq("reservations.errors"), eq(validRequest));
    }

    @Test
    void publishReservation_FlightNotFound() {
        when(validatorService.isValid(validRequest)).thenThrow(new FlightNotFoundException("Flight not found"));

        assertThrows(FlightNotFoundException.class, () -> publishReservation.publishReservation(validRequest));
    }

    @Test
    void publishReservation_SeatReserved() {
        when(validatorService.isValid(validRequest)).thenThrow(new InvalidSeatException("Seat is reserved"));

        assertThrows(InvalidSeatException.class, () -> publishReservation.publishReservation(validRequest));
    }
}
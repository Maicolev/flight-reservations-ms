package com.example.processing.service;

import com.example.common.dto.ReservationRequest;
import com.example.common.exceptions.InvalidReservationException;
import com.example.common.exceptions.ReservationNotFoundException;
import com.example.common.exceptions.SeatNotFoundException;
import com.example.common.model.Reservation;
import com.example.common.model.Seat;
import com.example.processing.repository.ReservationRepository;
import com.example.processing.repository.SeatRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationProcessorTest {

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private ReservationProcessorImpl processor;

    private final ReservationRequest validRequest = new ReservationRequest(1L, "12A", "test@example.com");
    private final Seat seat = new Seat();
    private final Reservation reservation = new Reservation();

    @Test
    void processPendingReservations_SeatNotFound() {
        when(seatRepository.findByFlightIdAndSeatNumber(anyLong(), anyString()))
                .thenReturn(Optional.empty());

        assertThrows(SeatNotFoundException.class,
                () -> processor.processPendingReservations(validRequest));
    }

    @Test
    void processPendingReservations_AlreadyReserved() {
        seat.setReserved(true);
        when(seatRepository.findByFlightIdAndSeatNumber(anyLong(), anyString()))
                .thenReturn(Optional.of(seat));

        processor.processPendingReservations(validRequest);

        verify(rabbitTemplate).convertAndSend(eq("reservations.errors"), eq(validRequest));
    }

    @Test
    void getConfirmedReservations_Success() {
        when(reservationRepository.findBySeatFlightIdAndStatus(anyLong(), any()))
                .thenReturn(List.of(reservation));

        List<Reservation> result = processor.getConfirmedReservations(1L);

        assertEquals(1, result.size());
    }

    @Test
    void cancelReservation_Success() {
        // Crear y configurar Seat
        Seat seat = new Seat();
        seat.setId(1L);
        seat.setReserved(true);

        reservation.setSeat(seat);
        reservation.setStatus(Reservation.ReservationStatus.CONFIRMED);

        when(reservationRepository.findById(anyLong()))
                .thenReturn(Optional.of(reservation));
        when(seatRepository.findById(anyLong()))
                .thenReturn(Optional.of(seat));

        processor.cancelReservation(1L);

        assertFalse(seat.isReserved());
        assertEquals(Reservation.ReservationStatus.CANCELLED, reservation.getStatus());
        verify(seatRepository).save(seat);
        verify(reservationRepository).save(reservation);
    }

    @Test
    void cancelReservation_NotFound() {
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ReservationNotFoundException.class,
                () -> processor.cancelReservation(1L));
    }

    @Test
    void cancelReservation_InvalidStatus() {
        reservation.setStatus(Reservation.ReservationStatus.PENDING);
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));

        assertThrows(InvalidReservationException.class,
                () -> processor.cancelReservation(1L));
    }
}
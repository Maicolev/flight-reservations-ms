package com.example.processing.service;

import com.example.common.exceptions.InvalidReservationException;
import com.example.common.exceptions.ReservationNotFoundException;
import com.example.common.model.Reservation;
import com.example.common.model.Seat;
import com.example.processing.repository.ReservationRepository;
import com.example.processing.repository.SeatRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationCancellerServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private ReservationCancellerServiceImpl canceller;

    private final Reservation reservation = new Reservation();
    private final Seat seat = new Seat();

    @Test
    void cancelReservation_Success() {
        reservation.setStatus(Reservation.ReservationStatus.CONFIRMED);
        reservation.setSeat(seat);
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));

        canceller.cancelReservation(1L);

        assertFalse(seat.isReserved());
        assertEquals(Reservation.ReservationStatus.CANCELLED, reservation.getStatus());
        verify(seatRepository).save(seat);
        verify(reservationRepository).save(reservation);
    }

    @Test
    void cancelReservation_NotFound() {
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ReservationNotFoundException.class,
                () -> canceller.cancelReservation(1L));
    }

    @Test
    void cancelReservation_InvalidStatus() {
        reservation.setStatus(Reservation.ReservationStatus.PENDING);
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));

        assertThrows(InvalidReservationException.class,
                () -> canceller.cancelReservation(1L));
    }
}
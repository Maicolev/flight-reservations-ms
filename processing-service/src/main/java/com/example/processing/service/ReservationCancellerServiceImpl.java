package com.example.processing.service;

import com.example.common.exceptions.InvalidReservationException;
import com.example.common.exceptions.ReservationNotFoundException;
import com.example.common.model.Reservation;
import com.example.common.model.Seat;
import com.example.processing.repository.ReservationRepository;
import com.example.processing.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class ReservationCancellerServiceImpl implements ReservationCanceller {
    private ReservationRepository reservationRepository;
    private SeatRepository seatRepository;

    @Transactional
    @Override
    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Booking not found"));

        if (reservation.getStatus() != Reservation.ReservationStatus.CONFIRMED) {
            throw new InvalidReservationException("Only confirmed reservations can be canceled");
        }

        Seat seat = reservation.getSeat();
        seat.setReserved(false);
        seatRepository.save(seat);

        reservation.setStatus(Reservation.ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
    }
}
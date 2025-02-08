package com.example.processing.service;

import com.example.common.dto.ReservationRequest;
import com.example.common.exceptions.InvalidReservationException;
import com.example.common.exceptions.ReservationNotFoundException;
import com.example.common.exceptions.SeatNotFoundException;
import com.example.common.model.Reservation;
import com.example.common.model.Seat;
import com.example.processing.repository.ReservationRepository;
import com.example.processing.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// processing-service/src/main/java/com/example/processing/service/ReservationProcessor.java
@Service
@RequiredArgsConstructor
public class ReservationProcessorImpl implements ReservationProcessor {

    private SeatRepository seatRepository;
    private ReservationRepository reservationRepository;
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "reservations.pending")
    @Transactional
    public void processPendingReservations(ReservationRequest request) {
        Seat seat = seatRepository.findByFlightIdAndSeatNumberWithLock(request.flightId(), request.seatNumber())
                .orElseThrow(() -> new SeatNotFoundException("Asiento no encontrado"));

        if (seat.isReserved()) {
            rabbitTemplate.convertAndSend("reservations.errors", request);
            return;
        }

        Reservation reservation = new Reservation();
        reservation.setSeat(seat);
        reservation.setEmail(request.email());
        reservation.setStatus(Reservation.ReservationStatus.CONFIRMED);
        reservationRepository.save(reservation);

        seat.setReserved(true);
        seatRepository.save(seat);

        rabbitTemplate.convertAndSend("reservations.confirmed", reservation.getId());
    }

    @Override
    public List<Reservation> getConfirmedReservations(Long flightId) {
        return reservationRepository.findBySeatFlightIdAndStatus(flightId, Reservation.ReservationStatus.CONFIRMED);
    }

    @Transactional
    @Override
    public void cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("Reserva no encontrada"));

        if (reservation.getStatus() != Reservation.ReservationStatus.CONFIRMED) {
            throw new InvalidReservationException("No se puede cancelar una reserva no confirmada");
        }

        reservation.getSeat().setReserved(false);
        reservation.setStatus(Reservation.ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
    }
}
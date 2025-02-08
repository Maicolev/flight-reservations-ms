package com.example.processing.repository;

import com.example.common.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findBySeatFlightIdAndStatus(Long flightId, Reservation.ReservationStatus reservationStatus);
}
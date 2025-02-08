package com.example.processing.repository;

import com.example.common.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    Optional<Seat> findByFlightIdAndSeatNumber(Long flightId, String seatNumber);
    Optional<Seat> findByFlightIdAndSeatNumberWithLock(Long flightId, String seatNumber);

}
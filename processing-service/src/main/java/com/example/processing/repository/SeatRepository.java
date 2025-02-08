package com.example.processing.repository;

import com.example.common.model.Seat;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

   // Optional<Seat> findByFlightIdAndSeatNumber(Long flightId, String seatNumber);

    @Lock(LockModeType.PESSIMISTIC_WRITE)  // Se aplica el bloqueo pesimista en esta consulta
    Optional<Seat> findByFlightIdAndSeatNumber(@Param("flightId") Long flightId,
                                               @Param("seatNumber") String seatNumber);
}
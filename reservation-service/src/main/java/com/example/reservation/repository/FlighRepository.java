package com.example.reservation.repository;

import com.example.common.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlighRepository extends JpaRepository<Flight, Long> {

    //@Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Flight> findById(@Param("id") Long id);
}
package com.example.common.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "flight_id", referencedColumnName = "id")
    private Flight flight;

    @Column(name = "seat_number")
    private String seatNumber;

    @Column(name = "reserved")
    private boolean reserved;

    @Column(name = "pending")
    private boolean pending;

    @Version
    @Column(name = "version")
    private Long version;
}
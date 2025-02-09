package com.example.common.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "flight_number")
    private String flightNumber;

    @Column(name = "departure")
    private String departure;

    @Column(name = "destination")
    private String destination;
}
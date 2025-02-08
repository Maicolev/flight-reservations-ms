package com.example.common.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record ReservationRequest(
        @NotNull Long flightId,
        @NotBlank String seatNumber,
        @Email String email
) implements Serializable {
        private static final long serialVersionUID = 1L;
}
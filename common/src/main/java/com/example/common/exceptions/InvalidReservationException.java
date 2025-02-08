package com.example.common.exceptions;

public class InvalidReservationException extends RuntimeException {
    public InvalidReservationException(String message) {
        super(message);
    }

    public InvalidReservationException(String message, Throwable cause) {
        super(message, cause);
    }
}
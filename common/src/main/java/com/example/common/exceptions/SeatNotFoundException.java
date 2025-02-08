package com.example.common.exceptions;

public class SeatNotFoundException extends RuntimeException {
    public SeatNotFoundException(String message) {
        super(message);
    }

    public SeatNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
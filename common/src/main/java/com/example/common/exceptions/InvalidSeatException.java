package com.example.common.exceptions;

public class InvalidSeatException extends RuntimeException {
    public InvalidSeatException(String message) {
        super(message);
    }

    public InvalidSeatException(String message, Throwable cause) {
        super(message, cause);
    }
}
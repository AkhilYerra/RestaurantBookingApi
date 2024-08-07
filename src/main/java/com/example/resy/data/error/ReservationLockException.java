package com.example.resy.data.error;

public class ReservationLockException extends RuntimeException {
    public ReservationLockException(String message) {
        super(message);
    }
}
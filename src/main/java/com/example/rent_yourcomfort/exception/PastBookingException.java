package com.example.rent_yourcomfort.exception;

public class PastBookingException extends RuntimeException {
    public PastBookingException(String message) {
        super(message);
    }
}

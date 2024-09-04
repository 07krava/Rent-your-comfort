package com.example.rent_yourcomfort.exception;

public class MaximumOccupantsExceededException extends RuntimeException {
    public MaximumOccupantsExceededException(String message) {
        super(message);
    }
}

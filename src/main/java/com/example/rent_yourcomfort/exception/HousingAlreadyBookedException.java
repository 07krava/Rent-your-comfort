package com.example.rent_yourcomfort.exception;

public class HousingAlreadyBookedException extends RuntimeException {
    public HousingAlreadyBookedException(String message) {
        super(message);
    }
}

package com.example.rent_yourcomfort.exception;

public class HousingNotFoundException extends RuntimeException {
    public HousingNotFoundException(String housingId) {
        super("Housing with ID " + housingId + " not found");
    }
}
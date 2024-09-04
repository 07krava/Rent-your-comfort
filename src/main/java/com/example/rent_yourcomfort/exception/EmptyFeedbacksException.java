package com.example.rent_yourcomfort.exception;

public class EmptyFeedbacksException extends RuntimeException {
    public EmptyFeedbacksException(String s) {
        super("Review from housingId is empty");
    }
}
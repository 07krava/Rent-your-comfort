package com.example.rent_yourcomfort.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorFeedback {
    EMPTY_FEEDBACKS("Review from housingId is empty."),
    HOUSING_NOT_FOUND("Housing not found with ID %s."),
    DEFAULT_ERROR("Unknown error occurred.");

    private String description;

    public String getDescription() {
        return description;
    }

    public String getDescriptionWithId(Long id) {
        return String.format(description, id);
    }
}

package com.example.restaurantplatform.exception;

import lombok.Getter;

@Getter
public enum ErrorMessage {

    GENERIC_ERROR("Generic Error occurred."),
    RESOURCE_NOT_FOUND("Resource does not exist."),
    UNAUTHORIZED("User is not authorized to access the resource."),
    INVALID_REQUEST("The request is not valid."),
    EMAIL_PASSWORD_NOT_FOUND("Email/password is incorrect."),
    USER_NOT_FOUND("User does not exist."),
    RESTAURANT_NOT_FOUND("Restaurant does not exist."),
    RESTAURANT_ALREADY_EXISTS_EMAIL("Restaurant already exists for email %s."),
    RESTAURANT_NOT_ALLOWED_TO_BE_DELETED("Restaurant is not allowed to be deleted."),
    USER_ALREADY_EXISTS("User already exists."),
    INVALID_ROLE("Invalid Role Provided."),
    PARAMETER_NOT_NULL("%s parameter cannot be null."),
    RESTAURANT_ALREADY_EXISTS_CITY("Restaurant already exists for city %s."),
    CURRENT_PASSWORD_INCORRECT("Current Password is not correct."),
    NOTHING_TO_UPDATE("Parameter values are null/empty for update request"),
    CURRENT_PASSWORD_REQUIRED("Current Password required for update request."),
    RESTAURANT_NOT_ALLOWED_TO_BE_UPDATED("Restaurant is not allowed to be updated.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}
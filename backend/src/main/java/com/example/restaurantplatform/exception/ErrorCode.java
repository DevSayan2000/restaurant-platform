package com.example.restaurantplatform.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    GENERIC_ERROR("RP-0001"),
    RESOURCE_NOT_FOUND("RP-0002"),
    UNAUTHORIZED("RP-0003"),
    INVALID_REQUEST("RP-0004"),
    EMAIL_PASSWORD_NOT_FOUND("RP-0005"),
    USER_NOT_FOUND("RP-0006"),
    RESTAURANT_NOT_FOUND("RP-0007"),
    RESTAURANT_ALREADY_EXISTS("RP-0008"),
    FORBIDDEN("RP-0009"),
    USER_ALREADY_EXISTS("RP-0010"),
    INVALID_ROLE("RP-0011"),
    PARAMETER_NOT_NULL("RP-0012"),
    CURRENT_PASSWORD_INCORRECT("RP-0013"),
    NOTHING_TO_UPDATE("RP-0014"),
    CURRENT_PASSWORD_REQUIRED("RP-0015"),
    RATING_DOES_NOT_EXIST("RP-0016"),
    NEW_PASSWORD_SAME_AS_CURRENT("RP-0017");


    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }
}
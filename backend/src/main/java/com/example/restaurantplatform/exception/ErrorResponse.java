package com.example.restaurantplatform.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {

    private final String errorCode;
    private final String errorMessage;

    public ErrorResponse(ErrorCode errorCode, ErrorMessage errorMessage) {
        this.errorCode = errorCode.getCode();
        this.errorMessage = errorMessage.getMessage();
    }
}
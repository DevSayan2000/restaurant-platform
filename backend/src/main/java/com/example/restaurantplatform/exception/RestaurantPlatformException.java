package com.example.restaurantplatform.exception;

import lombok.Getter;

@Getter
public class RestaurantPlatformException extends RuntimeException {

    private final ErrorCode errorCode;
    private final ErrorMessage errorMessage;

    public RestaurantPlatformException(ErrorCode errorCode, ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
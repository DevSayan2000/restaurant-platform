package com.example.restaurantplatform.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RestaurantPlatformException.class)
    public ResponseEntity<ErrorResponse> handleRestaurantPlatformException(
            RestaurantPlatformException ex) {

        ErrorResponse errorResponse = new ErrorResponse(
                ex.getErrorCode(),
                ex.getErrorMessage()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCode.UNAUTHORIZED,
                ErrorMessage.UNAUTHORIZED
        );

        return ResponseEntity
                .status(ex.getStatusCode())
                .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {

        log.error("Unhandled exception [{}]: {}", ex.getClass().getName(), ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCode.GENERIC_ERROR,
                ErrorMessage.GENERIC_ERROR
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }
}
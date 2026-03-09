package com.example.restaurantplatform.dto.general;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericResponse {
    private String message;
    private Long id;

    public GenericResponse(String message) {
        this.message = message;
        this.id = null;
    }
}
package com.example.restaurantplatform.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRatingRequest {
    private Integer rating;
    private String review;
}
package com.example.restaurantplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RestaurantResponse {

    private Long id;
    private String name;
    private String city;
    private String cuisine;
    private Double avgRating;
    private Double avgFootfall;
}
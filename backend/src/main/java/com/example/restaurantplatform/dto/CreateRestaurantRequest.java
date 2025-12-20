package com.example.restaurantplatform.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRestaurantRequest {
    private String name;
    private String city;
    private String area;
    private String cuisine;
}
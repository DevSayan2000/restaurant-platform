package com.example.restaurantplatform.dto.restaurant;

import com.example.restaurantplatform.enums.FoodType;
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
    private FoodType foodType;
    private String cuisine;
    private Double avgRating;
}
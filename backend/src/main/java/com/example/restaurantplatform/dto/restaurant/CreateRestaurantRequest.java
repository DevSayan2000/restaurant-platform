package com.example.restaurantplatform.dto.restaurant;

import com.example.restaurantplatform.enums.FoodType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRestaurantRequest {
    private String name;
    private String city;
    private FoodType foodType;
    private String cuisine;
}
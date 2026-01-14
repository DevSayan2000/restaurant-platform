package com.example.restaurantplatform.mapper;

import com.example.restaurantplatform.enums.FoodType;

public record PopularRestaurantMapper(
        Long id,
        String name,
        String city,
        FoodType foodType,
        String cuisine,
        Double avgRating
) {}
package com.example.restaurantplatform.dto.restaurant;

import com.example.restaurantplatform.enums.FoodType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
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
    private String createdBy;
    private LocalDateTime createdDate;
    private Boolean owner;

    public RestaurantResponse(Long id, String name, String city, FoodType foodType,
                              String cuisine, Double avgRating, String createdBy,
                              LocalDateTime createdDate) {
        this(id, name, city, foodType, cuisine, avgRating, createdBy, createdDate, null);
    }
}
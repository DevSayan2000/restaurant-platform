package com.example.restaurantplatform.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ListRestaurantResponse {

    List<RestaurantResponse> restaurantResponses;
}
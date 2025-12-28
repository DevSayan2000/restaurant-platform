package com.example.restaurantplatform.service;

import com.example.restaurantplatform.dto.CreateRestaurantRequest;
import com.example.restaurantplatform.dto.RestaurantResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RestaurantService {

    ResponseEntity<String> createRestaurant(CreateRestaurantRequest request);

    ResponseEntity<List<RestaurantResponse>> getRestaurants();

    ResponseEntity<List<RestaurantResponse>> getRestaurantsByCity(String city);
}
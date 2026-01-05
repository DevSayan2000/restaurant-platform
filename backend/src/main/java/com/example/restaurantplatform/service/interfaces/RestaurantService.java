package com.example.restaurantplatform.service.interfaces;

import com.example.restaurantplatform.dto.general.GenericResponse;
import com.example.restaurantplatform.dto.restaurant.CreateRestaurantRequest;
import com.example.restaurantplatform.dto.restaurant.RestaurantResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RestaurantService {

    ResponseEntity<GenericResponse> createRestaurant(CreateRestaurantRequest request);

    ResponseEntity<List<RestaurantResponse>> getRestaurants();

    ResponseEntity<List<RestaurantResponse>> getRestaurantsByCity(String city);

    ResponseEntity<GenericResponse> deleteRestaurant(Long restaurantId);
}
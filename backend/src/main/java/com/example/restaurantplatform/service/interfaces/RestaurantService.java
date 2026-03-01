package com.example.restaurantplatform.service.interfaces;

import com.example.restaurantplatform.dto.general.GenericResponse;
import com.example.restaurantplatform.dto.restaurant.CreateRestaurantRequest;
import com.example.restaurantplatform.dto.restaurant.ListRestaurantResponse;
import com.example.restaurantplatform.dto.restaurant.RestaurantResponse;
import com.example.restaurantplatform.dto.restaurant.UpdateRestaurantRequest;
import org.springframework.http.ResponseEntity;

public interface RestaurantService {

    ResponseEntity<GenericResponse> createRestaurant(CreateRestaurantRequest request);

    ResponseEntity<ListRestaurantResponse> getRestaurants();

    ResponseEntity<ListRestaurantResponse> getRestaurantsByCity(String city);

    ResponseEntity<RestaurantResponse> getRestaurantById(Long restaurantId);

    ResponseEntity<GenericResponse> deleteRestaurant(Long restaurantId);

    ResponseEntity<GenericResponse> updateRestaurant(Long restaurantId, UpdateRestaurantRequest request);
}
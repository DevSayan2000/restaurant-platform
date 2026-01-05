package com.example.restaurantplatform.service.interfaces;

import com.example.restaurantplatform.dto.general.GenericResponse;
import com.example.restaurantplatform.dto.restaurant.RestaurantResponse;
import com.example.restaurantplatform.dto.user.CreateUserRequest;
import com.example.restaurantplatform.dto.user.UserResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    ResponseEntity<GenericResponse> createUser(CreateUserRequest request);

    ResponseEntity<List<UserResponse>> getUsers();

    ResponseEntity<List<RestaurantResponse>> getAllRestaurantsForUsers();

    ResponseEntity<GenericResponse> deleteUser(Long userId);
}
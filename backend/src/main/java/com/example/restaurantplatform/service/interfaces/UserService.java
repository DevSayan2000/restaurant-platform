package com.example.restaurantplatform.service.interfaces;

import com.example.restaurantplatform.dto.general.GenericResponse;
import com.example.restaurantplatform.dto.restaurant.ListRestaurantResponse;
import com.example.restaurantplatform.dto.user.CreateUserRequest;
import com.example.restaurantplatform.dto.user.ListUserResponse;
import com.example.restaurantplatform.dto.user.Reviews;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<GenericResponse> createUser(CreateUserRequest request);

    ResponseEntity<ListUserResponse> getUsers();

    ResponseEntity<ListRestaurantResponse> getAllRestaurantsForUsers();

    ResponseEntity<Reviews> getAllReviewsGivenByUser();

    ResponseEntity<ListRestaurantResponse> getRestaurantsReviewedByUser();

    ResponseEntity<GenericResponse> deleteUser(Long userId);
}
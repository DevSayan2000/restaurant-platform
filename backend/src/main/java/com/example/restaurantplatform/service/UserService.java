package com.example.restaurantplatform.service;

import com.example.restaurantplatform.dto.CreateUserRequest;
import com.example.restaurantplatform.dto.UserResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    ResponseEntity<String> createUser(CreateUserRequest request);

    ResponseEntity<List<UserResponse>> getUsers();
}
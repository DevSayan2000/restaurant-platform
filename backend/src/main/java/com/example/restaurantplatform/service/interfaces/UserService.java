package com.example.restaurantplatform.service.interfaces;

import com.example.restaurantplatform.dto.user.CreateUserRequest;
import com.example.restaurantplatform.dto.user.UserResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    ResponseEntity<String> createUser(CreateUserRequest request);

    ResponseEntity<List<UserResponse>> getUsers();
}
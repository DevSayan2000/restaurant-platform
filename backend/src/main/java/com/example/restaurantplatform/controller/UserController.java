package com.example.restaurantplatform.controller;

import com.example.restaurantplatform.dto.CreateUserRequest;
import com.example.restaurantplatform.dto.UserResponse;
import com.example.restaurantplatform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // POST /api/users
    @PostMapping
    public ResponseEntity<String> createUser(
            @RequestBody CreateUserRequest request) {

        return userService.createUser(request);
    }

    // GET /api/users
    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers() {

        return userService.getUsers();
    }
}
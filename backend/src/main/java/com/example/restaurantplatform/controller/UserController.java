package com.example.restaurantplatform.controller;

import com.example.restaurantplatform.dto.user.CreateUserRequest;
import com.example.restaurantplatform.dto.user.UserResponse;
import com.example.restaurantplatform.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // POST /api/user
    @Operation(
            summary = "Create user",
            description = "Creates ROLE_RESTAURANT_ADMIN/ROLE_USER"
    )
    @PostMapping
    public ResponseEntity<String> createUser(
            @RequestBody CreateUserRequest request) {

        return userService.createUser(request);
    }

    // GET /api/user
    @Operation(
            summary = "Get user",
            description = "Get ROLE_RESTAURANT_ADMIN/ROLE_USER"
    )
    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers() {

        return userService.getUsers();
    }
}
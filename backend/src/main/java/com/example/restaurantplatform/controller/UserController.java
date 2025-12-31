package com.example.restaurantplatform.controller;

import com.example.restaurantplatform.dto.restaurant.RestaurantResponse;
import com.example.restaurantplatform.dto.user.CreateUserRequest;
import com.example.restaurantplatform.dto.user.UserResponse;
import com.example.restaurantplatform.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // POST /api/users
    @Operation(
            summary = "Create user",
            description = "Creates ROLE_RESTAURANT_ADMIN/ROLE_USER"
    )
    @PostMapping
    public ResponseEntity<String> createUser(
            @RequestBody CreateUserRequest request) {

        return userService.createUser(request);
    }

    // GET /api/users
    @Operation(
            summary = "Get user",
            description = "Get ROLE_RESTAURANT_ADMIN/ROLE_USER"
    )
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers() {

        return userService.getUsers();
    }

    // GET /api/users/restaurants
    @Operation(
            summary = "Get user",
            description = "Get ROLE_RESTAURANT_ADMIN/ROLE_USER"
    )
    @PreAuthorize("hasRole('USER')")
    @GetMapping("restaurants")
    public ResponseEntity<List<RestaurantResponse>> getAllRestaurantsForUsers() {

        return userService.getAllRestaurantsForUsers();
    }

    // DELETE /api/users
    @Operation(
            summary = "Create user",
            description = "Creates ROLE_RESTAURANT_ADMIN/ROLE_USER"
    )
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long userId) {

        return userService.deleteUser(userId);
    }
}
package com.example.restaurantplatform.controller;

import com.example.restaurantplatform.dto.restaurant.CreateRestaurantRequest;
import com.example.restaurantplatform.dto.restaurant.RestaurantResponse;
import com.example.restaurantplatform.service.interfaces.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    // POST /api/restaurants
    @Operation(
            summary = "Create restaurant",
            description = "Accessible only by Super_Admin, Restaurant_Admin"
    )
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','RESTAURANT_ADMIN')")
    @PostMapping
    public ResponseEntity<String> createRestaurant(
            @RequestBody CreateRestaurantRequest request) {

        return restaurantService.createRestaurant(request);
    }

    // GET /api/restaurants
    @Operation(
            summary = "Get restaurant",
            description = "Accessible only by Super_Admin, Restaurant_Admin"
    )
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','RESTAURANT_ADMIN')")
    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> getRestaurants() {

        return restaurantService.getRestaurants();
    }

    // GET /api/restaurants?city=Durgapur
    @Operation(
            summary = "Get restaurant by city",
            description = "Accessible only by Super_Admin, Restaurant_Admin"
    )
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_RESTAURANT_ADMIN')")
    @GetMapping(params = "city")
    public ResponseEntity<List<RestaurantResponse>> getRestaurantsByCity(
            @RequestParam String city) {

        return restaurantService.getRestaurantsByCity(city);
    }

    // DELETE /api/restaurants/{restaurantId}
    @Operation(
            summary = "Delete restaurant",
            description = "Accessible only by Super_Admin, Restaurant_Admin"
    )
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','RESTAURANT_ADMIN')")
    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<String> deleteRestaurant(
            @PathVariable Long restaurantId) {

        return restaurantService.deleteRestaurant(restaurantId);
    }
}
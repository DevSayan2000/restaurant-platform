package com.example.restaurantplatform.controller;

import com.example.restaurantplatform.dto.CreateRestaurantRequest;
import com.example.restaurantplatform.dto.RestaurantResponse;
import com.example.restaurantplatform.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    // POST /api/restaurants
    @PostMapping
    public ResponseEntity<String> createRestaurant(
            @RequestBody CreateRestaurantRequest request) {

        return restaurantService.createRestaurant(request);
    }

    // GET /api/restaurants
    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> getRestaurantsByCity() {

        return restaurantService.getRestaurants();
    }

    // GET /api/restaurants?city=Durgapur
    @GetMapping(params = "city")
    public ResponseEntity<List<RestaurantResponse>> getRestaurantsByCity(
            @RequestParam String city) {

        return restaurantService.getRestaurantsByCity(city);
    }
}
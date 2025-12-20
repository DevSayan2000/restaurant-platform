package com.example.restaurantplatform.controller;

import com.example.restaurantplatform.dto.CreateRestaurantRequest;
import com.example.restaurantplatform.dto.RestaurantResponse;
import com.example.restaurantplatform.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<RestaurantResponse> createRestaurant(
            @RequestBody CreateRestaurantRequest request) {

        restaurantService.createRestaurant(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public List<RestaurantResponse> getRestaurants(
            @RequestParam String city) {

        return restaurantService.getRestaurantsByCity(city);
    }
}
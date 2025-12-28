package com.example.restaurantplatform.controller;

import com.example.restaurantplatform.dto.CreateRatingRequest;
import com.example.restaurantplatform.service.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    // POST /api/restaurants/{restaurantId}/ratings?userId=1
    @PostMapping
    public ResponseEntity<String> addOrUpdateRating(
            @PathVariable Long restaurantId,
            @RequestParam Long userId,
            @Valid @RequestBody CreateRatingRequest request) {

        return ratingService.addOrUpdateRating(restaurantId, userId, request);
    }

    // GET /api/restaurants/{restaurantId}/ratings/average
    @GetMapping("/average")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long restaurantId) {
        return ratingService.getAverageRating(restaurantId);
    }
}
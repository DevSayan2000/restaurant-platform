package com.example.restaurantplatform.controller;

import com.example.restaurantplatform.dto.rating.CreateRatingRequest;
import com.example.restaurantplatform.service.interfaces.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/restaurants/{restaurantId}/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    // POST /api/restaurants/{restaurantId}/ratings
    @Operation(
            summary = "Add or update rating",
            description = "Accessible only by User"
    )
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<String> addOrUpdateRating(
            @PathVariable Long restaurantId,
            @Valid @RequestBody CreateRatingRequest request) {

        return ratingService.addOrUpdateRating(restaurantId, request);
    }

    // GET /api/restaurants/{restaurantId}/ratings/average
    @Operation(
            summary = "Get average rating",
            description = "Accessible by Super_Admin/Restaurant_Admin/User"
    )
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','RESTAURANT_ADMIN','USER')")
    @GetMapping("/average")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long restaurantId) {
        return ratingService.getAverageRating(restaurantId);
    }

    // GET /api/restaurants/{restaurantId}/ratings/reviews
    @Operation(
            summary = "Get all reviews for that restaurant",
            description = "Accessible by Super_Admin/Restaurant_Admin/User"
    )
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','RESTAURANT_ADMIN','USER')")
    @GetMapping("/reviews")
    public ResponseEntity<List<String>> getAllReviews(@PathVariable Long restaurantId) {
        return ratingService.getAllReviews(restaurantId);
    }
}
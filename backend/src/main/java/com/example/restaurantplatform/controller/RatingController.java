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

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/restaurants/{restaurantId}/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    // POST /api/restaurants/{restaurantId}/rating
    @Operation(
            summary = "Add or update rating",
            description = "Accessible only by ROLE_USER"
    )
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<String> addOrUpdateRating(
            @PathVariable Long restaurantId,
            @Valid @RequestBody CreateRatingRequest request) {

        return ratingService.addOrUpdateRating(restaurantId, request);
    }

    // GET /api/restaurants/{restaurantId}/rating/average
    @Operation(
            summary = "Get average rating",
            description = "Accessible only by ROLE_USER"
    )
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/average")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long restaurantId) {
        return ratingService.getAverageRating(restaurantId);
    }
}
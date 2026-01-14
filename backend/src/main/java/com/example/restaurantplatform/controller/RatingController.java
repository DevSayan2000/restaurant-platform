package com.example.restaurantplatform.controller;

import com.example.restaurantplatform.dto.general.GenericResponse;
import com.example.restaurantplatform.dto.rating.AllReviewsResponse;
import com.example.restaurantplatform.dto.rating.AverageRatingResponse;
import com.example.restaurantplatform.dto.rating.CreateRatingRequest;
import com.example.restaurantplatform.service.interfaces.RatingService;
import com.example.restaurantplatform.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully added or updated ratings",
                    content = @Content(
                            mediaType = MediaType.TEXT_PLAIN_VALUE,
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error occurred",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content()
            )
    })
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<GenericResponse> addOrUpdateRating(
            @PathVariable Long restaurantId,
            @Valid @RequestBody CreateRatingRequest request) {

        return ratingService.addOrUpdateRating(restaurantId, request);
    }

    // GET /api/restaurants/{restaurantId}/ratings/average
    @Operation(
            summary = "Get average rating",
            description = "Accessible by Super_Admin/Restaurant_Admin/User"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched average ratings for a restaurant",
                    content = @Content(
                            mediaType = MediaType.TEXT_PLAIN_VALUE,
                            schema = @Schema(implementation = Double.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error occurred",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content()
            )
    })
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','RESTAURANT_ADMIN','USER')")
    @GetMapping("/average")
    public ResponseEntity<AverageRatingResponse> getAverageRating(@PathVariable Long restaurantId) {
        return ratingService.getAverageRating(restaurantId);
    }

    // GET /api/restaurants/{restaurantId}/ratings/reviews
    @Operation(
            summary = "Get all reviews for that restaurant",
            description = "Accessible by Super_Admin/Restaurant_Admin/User"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched all reviews for a restaurant",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AllReviewsResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error occurred",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content()
            )
    })
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','RESTAURANT_ADMIN','USER')")
    @GetMapping("/reviews")
    public ResponseEntity<AllReviewsResponse> getAllReviews(@PathVariable Long restaurantId) {
        return ratingService.getAllReviews(restaurantId);
    }
}
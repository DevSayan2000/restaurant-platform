package com.example.restaurantplatform.controller;

import com.example.restaurantplatform.dto.analytics.AnalyticsResponse;
import com.example.restaurantplatform.dto.analytics.PopularRestaurants;
import com.example.restaurantplatform.dto.analytics.PopularReviews;
import com.example.restaurantplatform.exception.ErrorResponse;
import com.example.restaurantplatform.service.interfaces.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    // GET /api/analytics
    @Operation(
            summary = "Get analytics details",
            description = "Accessible by Super_Admin/Restaurant_Admin/User"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched analytics reports",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AnalyticsResponse.class)
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
    @GetMapping
    public ResponseEntity<AnalyticsResponse> getAnalytics() {
        return analyticsService.getAnalytics();
    }

    // GET /api/analytics/popularRestaurants
    @Operation(
            summary = "Get top 3 popular restaurant details",
            description = "Accessible only by User"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched top 3 popular restaurants",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PopularRestaurants.class)
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
    @GetMapping("/popularRestaurants")
    public ResponseEntity<PopularRestaurants> getPopularRestaurants() {
        return analyticsService.getPopularRestaurants();
    }

    // GET /api/analytics/popularReviews
    @Operation(
            summary = "Get top 3 popular reviews details",
            description = "Accessible only by Restaurant_Admin"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched top 3 popular reviews",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PopularReviews.class)
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
    @PreAuthorize("hasRole('RESTAURANT_ADMIN')")
    @GetMapping("/popularReviews")
    public ResponseEntity<PopularReviews> getPopularReviews() {
        return analyticsService.getPopularReviews();
    }
}
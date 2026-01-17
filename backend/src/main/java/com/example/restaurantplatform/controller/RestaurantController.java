package com.example.restaurantplatform.controller;

import com.example.restaurantplatform.dto.general.GenericResponse;
import com.example.restaurantplatform.dto.restaurant.CreateRestaurantRequest;
import com.example.restaurantplatform.dto.restaurant.ListRestaurantResponse;
import com.example.restaurantplatform.dto.restaurant.RestaurantResponse;
import com.example.restaurantplatform.exception.ErrorResponse;
import com.example.restaurantplatform.service.interfaces.RestaurantService;
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
import org.springframework.web.bind.annotation.*;

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
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Successfully created a restaurant",
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
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','RESTAURANT_ADMIN')")
    @PostMapping
    public ResponseEntity<GenericResponse> createRestaurant(
            @RequestBody CreateRestaurantRequest request) {

        return restaurantService.createRestaurant(request);
    }

    // GET /api/restaurants
    @Operation(
            summary = "Get restaurant",
            description = "Accessible only by Super_Admin, Restaurant_Admin"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched all restaurant details",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ListRestaurantResponse.class)
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
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','RESTAURANT_ADMIN')")
    @GetMapping
    public ResponseEntity<ListRestaurantResponse> getRestaurants() {

        return restaurantService.getRestaurants();
    }

    // GET /api/restaurants?city=Durgapur
    @Operation(
            summary = "Get restaurant by city",
            description = "Accessible only by Super_Admin, Restaurant_Admin"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched all restaurant details for a city",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ListRestaurantResponse.class)
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
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_RESTAURANT_ADMIN')")
    @GetMapping(params = "city")
    public ResponseEntity<ListRestaurantResponse> getRestaurantsByCity(
            @RequestParam String city) {

        return restaurantService.getRestaurantsByCity(city);
    }

    // GET /api/restaurants/{restaurantId}
    @Operation(
            summary = "Get restaurant by Id",
            description = "Accessible by User, Super_Admin, Restaurant_Admin"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully gets a restaurant",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RestaurantResponse.class)
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
    @PreAuthorize("hasAnyRole('USER','SUPER_ADMIN','RESTAURANT_ADMIN')")
    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantResponse> getRestaurantById(
            @PathVariable Long restaurantId) {

        return restaurantService.getRestaurantById(restaurantId);
    }

    // DELETE /api/restaurants/{restaurantId}
    @Operation(
            summary = "Delete restaurant",
            description = "Accessible only by Super_Admin, Restaurant_Admin"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully deleted a restaurant",
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
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','RESTAURANT_ADMIN')")
    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<GenericResponse> deleteRestaurant(
            @PathVariable Long restaurantId) {

        return restaurantService.deleteRestaurant(restaurantId);
    }
}
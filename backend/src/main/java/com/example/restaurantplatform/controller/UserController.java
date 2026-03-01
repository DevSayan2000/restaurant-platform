package com.example.restaurantplatform.controller;

import com.example.restaurantplatform.dto.general.GenericResponse;
import com.example.restaurantplatform.dto.restaurant.ListRestaurantResponse;
import com.example.restaurantplatform.dto.user.CreateUserRequest;
import com.example.restaurantplatform.dto.user.ListUserResponse;
import com.example.restaurantplatform.dto.user.Reviews;
import com.example.restaurantplatform.dto.user.UpdateUserRequest;
import com.example.restaurantplatform.exception.ErrorResponse;
import com.example.restaurantplatform.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // POST /api/users
    @Operation(
            summary = "Create user",
            description = "Creates Restaurant_Admin/User"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Successfully created an user",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GenericResponse.class)
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
    @PostMapping
    public ResponseEntity<GenericResponse> createUser(
            @Valid @RequestBody CreateUserRequest request) {

        return userService.createUser(request);
    }

    // PUT /api/users
    @Operation(
            summary = "Update user details",
            description = "Updates Restaurant_Admin/User Name and Password"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully updated an user",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GenericResponse.class)
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
    @PutMapping
    public ResponseEntity<GenericResponse> updateUser(
            @Valid @RequestBody UpdateUserRequest request) {

        return userService.updateUser(request);
    }

    // GET /api/users
    @Operation(
            summary = "Get all users",
            description = "Accessible only by Super_Admin"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Successfully fetch all user details",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ListUserResponse.class)
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
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping
    public ResponseEntity<ListUserResponse> getUsers() {

        return userService.getUsers();
    }

    // GET /api/users/restaurants
    @Operation(
            summary = "Get all restaurant details for users",
            description = "Accessible only by User"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched all restaurants details for user",
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
    @PreAuthorize("hasRole('USER')")
    @GetMapping("restaurants")
    public ResponseEntity<ListRestaurantResponse> getAllRestaurantsForUsers() {

        return userService.getAllRestaurantsForUsers();
    }

    // GET /api/users/reviews
    @Operation(
            summary = "Get all reviews given by user",
            description = "Accessible only by User"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched all reviews given by user",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Reviews.class)
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
    @GetMapping("reviews")
    public ResponseEntity<Reviews> getAllReviewsGivenByUser() {

        return userService.getAllReviewsGivenByUser();
    }

    // GET /api/users/reviewedRestaurants
    @Operation(
            summary = "Get all restaurant reviewed by user",
            description = "Accessible only by User"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched all restaurants reviewed by user",
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
    @PreAuthorize("hasRole('USER')")
    @GetMapping("reviewedRestaurants")
    public ResponseEntity<ListRestaurantResponse> getRestaurantsReviewedByUser() {

        return userService.getRestaurantsReviewedByUser();
    }

    // DELETE /api/users
    @Operation(
            summary = "Delete user",
            description = "Accessible only by Super_Admin"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully deleted an user",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GenericResponse.class)
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
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<GenericResponse> deleteUser(
            @PathVariable Long userId) {

        return userService.deleteUser(userId);
    }
}
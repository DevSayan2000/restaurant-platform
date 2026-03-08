package com.example.restaurantplatform.controller;

import com.example.restaurantplatform.dto.general.GenericResponse;
import com.example.restaurantplatform.dto.menu.CreateMenuItemRequest;
import com.example.restaurantplatform.dto.menu.MenuResponse;
import com.example.restaurantplatform.dto.menu.UpdateMenuItemRequest;
import com.example.restaurantplatform.exception.ErrorResponse;
import com.example.restaurantplatform.service.interfaces.MenuService;
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
@RequestMapping("/restaurants/{restaurantId}/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    // POST /api/restaurants/{restaurantId}/menu
    @Operation(
            summary = "Add menu item",
            description = "Only the restaurant admin who owns this restaurant can add menu items"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Menu item added successfully",
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
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content())
    })
    @PreAuthorize("hasRole('RESTAURANT_ADMIN')")
    @PostMapping
    public ResponseEntity<GenericResponse> addMenuItem(
            @PathVariable Long restaurantId,
            @Valid @RequestBody CreateMenuItemRequest request) {
        return menuService.addMenuItem(restaurantId, request);
    }

    // PUT /api/restaurants/{restaurantId}/menu/{menuItemId}
    @Operation(
            summary = "Update menu item",
            description = "Only the restaurant admin who owns this restaurant can update menu items"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Menu item updated successfully",
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
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content())
    })
    @PreAuthorize("hasRole('RESTAURANT_ADMIN')")
    @PutMapping("/{menuItemId}")
    public ResponseEntity<GenericResponse> updateMenuItem(
            @PathVariable Long restaurantId,
            @PathVariable Long menuItemId,
            @RequestBody UpdateMenuItemRequest request) {
        return menuService.updateMenuItem(restaurantId, menuItemId, request);
    }

    // DELETE /api/restaurants/{restaurantId}/menu/{menuItemId}
    @Operation(
            summary = "Delete menu item",
            description = "Only the restaurant admin who owns this restaurant can delete menu items"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Menu item deleted successfully",
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
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content())
    })
    @PreAuthorize("hasRole('RESTAURANT_ADMIN')")
    @DeleteMapping("/{menuItemId}")
    public ResponseEntity<GenericResponse> deleteMenuItem(
            @PathVariable Long restaurantId,
            @PathVariable Long menuItemId) {
        return menuService.deleteMenuItem(restaurantId, menuItemId);
    }

    // GET /api/restaurants/{restaurantId}/menu
    @Operation(
            summary = "Get restaurant menu",
            description = "Accessible by all authenticated users. Returns menu items grouped by category."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved the menu",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MenuResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error occurred",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PreAuthorize("hasAnyRole('USER','SUPER_ADMIN','RESTAURANT_ADMIN')")
    @GetMapping
    public ResponseEntity<MenuResponse> getMenu(@PathVariable Long restaurantId) {
        return menuService.getMenu(restaurantId);
    }

    // GET /api/restaurants/{restaurantId}/menu?category=APPETIZER
    @Operation(
            summary = "Get menu by category",
            description = "Accessible by all authenticated users. Filter menu items by category (APPETIZER, MAIN_COURSE, DESSERT)."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved menu items for the category",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MenuResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error occurred",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PreAuthorize("hasAnyRole('USER','SUPER_ADMIN','RESTAURANT_ADMIN')")
    @GetMapping(params = "category")
    public ResponseEntity<MenuResponse> getMenuByCategory(
            @PathVariable Long restaurantId,
            @RequestParam String category) {
        return menuService.getMenuByCategory(restaurantId, category);
    }
}





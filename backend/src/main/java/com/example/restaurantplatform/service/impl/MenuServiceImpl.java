package com.example.restaurantplatform.service.impl;

import com.example.restaurantplatform.dto.general.GenericResponse;
import com.example.restaurantplatform.dto.menu.CreateMenuItemRequest;
import com.example.restaurantplatform.dto.menu.MenuItemResponse;
import com.example.restaurantplatform.dto.menu.MenuResponse;
import com.example.restaurantplatform.dto.menu.UpdateMenuItemRequest;
import com.example.restaurantplatform.entity.MenuItem;
import com.example.restaurantplatform.entity.Restaurant;
import com.example.restaurantplatform.enums.MenuCategory;
import com.example.restaurantplatform.exception.ErrorCode;
import com.example.restaurantplatform.exception.ErrorMessage;
import com.example.restaurantplatform.exception.RestaurantPlatformException;
import com.example.restaurantplatform.repository.MenuItemRepository;
import com.example.restaurantplatform.repository.RestaurantRepository;
import com.example.restaurantplatform.service.interfaces.MenuService;
import com.example.restaurantplatform.util.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuServiceImpl implements MenuService {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final CommonUtils commonUtils;

    @Transactional
    @Override
    public ResponseEntity<GenericResponse> addMenuItem(Long restaurantId, CreateMenuItemRequest request) {
        Restaurant restaurant = getRestaurantAndValidateOwnership(restaurantId);

        // Check for duplicate menu item name in same restaurant
        menuItemRepository.findByRestaurantIdAndNameIgnoreCase(restaurantId, request.getName())
                .ifPresent(existing -> {
                    throw new RestaurantPlatformException(ErrorCode.MENU_ITEM_ALREADY_EXISTS, ErrorMessage.MENU_ITEM_ALREADY_EXISTS, request.getName());
                });

        MenuItem menuItem = new MenuItem();
        menuItem.setRestaurant(restaurant);
        menuItem.setName(request.getName());
        menuItem.setDescription(request.getDescription());
        menuItem.setPrice(request.getPrice());
        menuItem.setCategory(request.getCategory());
        menuItem.setVegetarian(request.isVegetarian());
        menuItem.setAvailable(request.isAvailable());

        menuItemRepository.save(menuItem);

        return new ResponseEntity<>(new GenericResponse("Menu item added successfully"), HttpStatus.CREATED);
    }

    @Transactional
    @Override
    public ResponseEntity<GenericResponse> updateMenuItem(Long restaurantId, Long menuItemId, UpdateMenuItemRequest request) {
        getRestaurantAndValidateOwnership(restaurantId);

        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new RestaurantPlatformException(ErrorCode.MENU_ITEM_NOT_FOUND, ErrorMessage.MENU_ITEM_NOT_FOUND));

        if (!menuItem.getRestaurant().getId().equals(restaurantId)) {
            throw new RestaurantPlatformException(ErrorCode.FORBIDDEN, ErrorMessage.MENU_ITEM_NOT_BELONGS_TO_RESTAURANT);
        }

        boolean updated = false;

        if (request.getName() != null && !request.getName().isBlank()) {
            // Check duplicate name only if name is being changed
            if (!request.getName().equalsIgnoreCase(menuItem.getName())) {
                menuItemRepository.findByRestaurantIdAndNameIgnoreCase(restaurantId, request.getName())
                        .ifPresent(existing -> {
                            throw new RestaurantPlatformException(ErrorCode.MENU_ITEM_ALREADY_EXISTS, ErrorMessage.MENU_ITEM_ALREADY_EXISTS, request.getName());
                        });
            }
            menuItem.setName(request.getName());
            updated = true;
        }

        if (request.getDescription() != null) {
            menuItem.setDescription(request.getDescription());
            updated = true;
        }

        if (request.getPrice() != null) {
            menuItem.setPrice(request.getPrice());
            updated = true;
        }

        if (request.getCategory() != null) {
            menuItem.setCategory(request.getCategory());
            updated = true;
        }

        if (request.getVegetarian() != null) {
            menuItem.setVegetarian(request.getVegetarian());
            updated = true;
        }

        if (request.getAvailable() != null) {
            menuItem.setAvailable(request.getAvailable());
            updated = true;
        }

        if (!updated) {
            throw new RestaurantPlatformException(ErrorCode.NOTHING_TO_UPDATE, ErrorMessage.NOTHING_TO_UPDATE);
        }

        menuItemRepository.save(menuItem);

        return new ResponseEntity<>(new GenericResponse("Menu item updated successfully"), HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity<GenericResponse> deleteMenuItem(Long restaurantId, Long menuItemId) {
        getRestaurantAndValidateOwnership(restaurantId);

        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new RestaurantPlatformException(ErrorCode.MENU_ITEM_NOT_FOUND, ErrorMessage.MENU_ITEM_NOT_FOUND));

        if (!menuItem.getRestaurant().getId().equals(restaurantId)) {
            throw new RestaurantPlatformException(ErrorCode.FORBIDDEN, ErrorMessage.MENU_ITEM_NOT_BELONGS_TO_RESTAURANT);
        }

        menuItemRepository.delete(menuItem);

        return new ResponseEntity<>(new GenericResponse("Menu item deleted successfully"), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<MenuResponse> getMenu(Long restaurantId) {
        // Validate restaurant exists
        restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantPlatformException(ErrorCode.RESTAURANT_NOT_FOUND, ErrorMessage.RESTAURANT_NOT_FOUND));

        List<MenuItem> items = menuItemRepository.findByRestaurantIdOrderByCategoryAscNameAsc(restaurantId);

        return buildMenuResponse(items);
    }

    @Override
    public ResponseEntity<MenuResponse> getMenuByCategory(Long restaurantId, String category) {
        // Validate restaurant exists
        restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantPlatformException(ErrorCode.RESTAURANT_NOT_FOUND, ErrorMessage.RESTAURANT_NOT_FOUND));

        MenuCategory menuCategory;
        try {
            menuCategory = MenuCategory.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RestaurantPlatformException(ErrorCode.INVALID_REQUEST, ErrorMessage.INVALID_MENU_CATEGORY);
        }

        List<MenuItem> items = menuItemRepository.findByRestaurantIdAndCategoryOrderByNameAsc(restaurantId, menuCategory);

        return buildMenuResponse(items);
    }

    /**
     * Validates restaurant exists and current user is the owner (restaurant admin).
     */
    private Restaurant getRestaurantAndValidateOwnership(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantPlatformException(ErrorCode.RESTAURANT_NOT_FOUND, ErrorMessage.RESTAURANT_NOT_FOUND));

        String email = commonUtils.getEmailAndRoleFromAuthToken().get("email");
        if (!restaurant.getEmail().equals(email)) {
            throw new RestaurantPlatformException(ErrorCode.FORBIDDEN, ErrorMessage.MENU_NOT_ALLOWED);
        }

        return restaurant;
    }

    private ResponseEntity<MenuResponse> buildMenuResponse(List<MenuItem> items) {
        // Group by category in a defined order: APPETIZER -> MAIN_COURSE -> DESSERT
        Map<String, List<MenuItemResponse>> groupedMenu = new LinkedHashMap<>();

        for (MenuCategory cat : MenuCategory.values()) {
            List<MenuItemResponse> categoryItems = items.stream()
                    .filter(item -> item.getCategory() == cat)
                    .map(this::toResponse)
                    .collect(Collectors.toList());

            if (!categoryItems.isEmpty()) {
                groupedMenu.put(formatCategoryName(cat), categoryItems);
            }
        }

        return new ResponseEntity<>(new MenuResponse(groupedMenu, items.size()), HttpStatus.OK);
    }

    private MenuItemResponse toResponse(MenuItem item) {
        return new MenuItemResponse(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.getCategory(),
                item.isVegetarian(),
                item.isAvailable(),
                item.getCreatedAt(),
                item.getUpdatedAt()
        );
    }

    private String formatCategoryName(MenuCategory category) {
        return switch (category) {
            case APPETIZER -> "Appetizers";
            case MAIN_COURSE -> "Main Course";
            case DESSERT -> "Desserts";
        };
    }
}


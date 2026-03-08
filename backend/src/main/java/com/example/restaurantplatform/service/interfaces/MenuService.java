package com.example.restaurantplatform.service.interfaces;

import com.example.restaurantplatform.dto.general.GenericResponse;
import com.example.restaurantplatform.dto.menu.CreateMenuItemRequest;
import com.example.restaurantplatform.dto.menu.MenuResponse;
import com.example.restaurantplatform.dto.menu.UpdateMenuItemRequest;
import org.springframework.http.ResponseEntity;

public interface MenuService {

    ResponseEntity<GenericResponse> addMenuItem(Long restaurantId, CreateMenuItemRequest request);

    ResponseEntity<GenericResponse> updateMenuItem(Long restaurantId, Long menuItemId, UpdateMenuItemRequest request);

    ResponseEntity<GenericResponse> deleteMenuItem(Long restaurantId, Long menuItemId);

    ResponseEntity<MenuResponse> getMenu(Long restaurantId);

    ResponseEntity<MenuResponse> getMenuByCategory(Long restaurantId, String category);
}


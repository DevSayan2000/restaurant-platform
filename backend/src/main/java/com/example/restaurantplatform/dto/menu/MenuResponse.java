package com.example.restaurantplatform.dto.menu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class MenuResponse {

    private Map<String, List<MenuItemResponse>> menu;
    private int totalItems;
}


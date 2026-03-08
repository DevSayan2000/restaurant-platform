package com.example.restaurantplatform.dto.menu;

import com.example.restaurantplatform.enums.MenuCategory;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateMenuItemRequest {

    private String name;

    private String description;

    private BigDecimal price;

    private MenuCategory category;

    private Boolean vegetarian;

    private Boolean available;
}


package com.example.restaurantplatform.dto.menu;

import com.example.restaurantplatform.enums.MenuCategory;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
public class MenuItemResponse {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private MenuCategory category;
    private boolean vegetarian;
    private boolean available;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}


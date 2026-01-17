package com.example.restaurantplatform.mapper;

import java.time.LocalDateTime;

public record ReviewMapper(
        String restaurant,
        String review,
        Integer rating,
        LocalDateTime createdDate
) {}
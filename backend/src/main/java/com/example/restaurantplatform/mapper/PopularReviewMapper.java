package com.example.restaurantplatform.mapper;

import java.time.LocalDateTime;

public record PopularReviewMapper(
        String review,
        String createdBy,
        LocalDateTime createdDate
) {}
package com.example.restaurantplatform.mapper;

import java.time.LocalDateTime;

public record RecentReviewMapper(
        String review,
        String createdBy,
        LocalDateTime createdDate
) {}
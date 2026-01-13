package com.example.restaurantplatform.dto.rating;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ReviewResponse {
    private Long id;
    private Integer rating;
    private String review;
    private LocalDateTime createdDate;
    private String createdBy;
    private Long userId;
}
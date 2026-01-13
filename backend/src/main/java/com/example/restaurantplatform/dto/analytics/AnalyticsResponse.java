package com.example.restaurantplatform.dto.analytics;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AnalyticsResponse {

    private Integer reviews;
    private Double rating;
    private Integer users;
    private Integer restaurants;
}
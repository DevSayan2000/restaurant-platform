package com.example.restaurantplatform.dto.analytics;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnalyticsResponse {

    private Integer reviews;
    private Double rating;
    private Integer users;
    private Integer restaurants;
}
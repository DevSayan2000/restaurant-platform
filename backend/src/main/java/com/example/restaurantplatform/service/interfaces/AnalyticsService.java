package com.example.restaurantplatform.service.interfaces;

import com.example.restaurantplatform.dto.analytics.AnalyticsResponse;
import com.example.restaurantplatform.dto.analytics.PopularRestaurants;
import com.example.restaurantplatform.dto.analytics.PopularReviews;
import org.springframework.http.ResponseEntity;

public interface AnalyticsService {

    ResponseEntity<AnalyticsResponse> getAnalytics();

    ResponseEntity<PopularRestaurants> getPopularRestaurants();

    ResponseEntity<PopularReviews> getPopularReviews();
}
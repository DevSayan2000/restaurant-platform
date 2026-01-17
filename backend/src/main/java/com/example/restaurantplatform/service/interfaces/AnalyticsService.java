package com.example.restaurantplatform.service.interfaces;

import com.example.restaurantplatform.dto.analytics.AnalyticsResponse;
import com.example.restaurantplatform.dto.analytics.PopularRestaurants;
import com.example.restaurantplatform.dto.analytics.RecentReviews;
import org.springframework.http.ResponseEntity;

public interface AnalyticsService {

    ResponseEntity<AnalyticsResponse> getAnalytics();

    ResponseEntity<PopularRestaurants> getPopularRestaurants();

    ResponseEntity<RecentReviews> getRecentReviews();
}
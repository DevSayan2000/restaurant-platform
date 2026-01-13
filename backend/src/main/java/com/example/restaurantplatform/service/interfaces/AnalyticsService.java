package com.example.restaurantplatform.service.interfaces;

import com.example.restaurantplatform.dto.analytics.AnalyticsResponse;
import org.springframework.http.ResponseEntity;

public interface AnalyticsService {

    ResponseEntity<AnalyticsResponse> getAnalytics();
}
package com.example.restaurantplatform.service;

import com.example.restaurantplatform.dto.CreateRatingRequest;
import org.springframework.http.ResponseEntity;

public interface RatingService {

    ResponseEntity<String> addOrUpdateRating(Long restaurantId, Long userId, CreateRatingRequest request);

    ResponseEntity<Double> getAverageRating(Long restaurantId);
}
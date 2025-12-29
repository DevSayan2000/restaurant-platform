package com.example.restaurantplatform.service.interfaces;

import com.example.restaurantplatform.dto.rating.CreateRatingRequest;
import org.springframework.http.ResponseEntity;

public interface RatingService {

    ResponseEntity<String> addOrUpdateRating(Long restaurantId, CreateRatingRequest request);

    ResponseEntity<Double> getAverageRating(Long restaurantId);
}
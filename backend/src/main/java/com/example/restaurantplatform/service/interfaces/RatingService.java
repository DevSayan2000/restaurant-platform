package com.example.restaurantplatform.service.interfaces;

import com.example.restaurantplatform.dto.general.GenericResponse;
import com.example.restaurantplatform.dto.rating.AverageRatingResponse;
import com.example.restaurantplatform.dto.rating.CreateRatingRequest;
import com.example.restaurantplatform.dto.rating.ReviewResponse;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RatingService {

    ResponseEntity<GenericResponse> addOrUpdateRating(Long restaurantId, CreateRatingRequest request);

    ResponseEntity<AverageRatingResponse> getAverageRating(Long restaurantId);

    ResponseEntity<List<ReviewResponse>> getAllReviews(Long restaurantId);
}
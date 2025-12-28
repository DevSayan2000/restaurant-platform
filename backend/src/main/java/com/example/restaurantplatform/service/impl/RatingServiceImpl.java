package com.example.restaurantplatform.service.impl;

import com.example.restaurantplatform.dto.CreateRatingRequest;
import com.example.restaurantplatform.entity.Rating;
import com.example.restaurantplatform.entity.Restaurant;
import com.example.restaurantplatform.entity.User;
import com.example.restaurantplatform.repository.RatingRepository;
import com.example.restaurantplatform.repository.RestaurantRepository;
import com.example.restaurantplatform.repository.UserRepository;
import com.example.restaurantplatform.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<String> addOrUpdateRating(Long restaurantId, Long userId, CreateRatingRequest request) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Rating rating = ratingRepository
                .findByRestaurantIdAndUserId(restaurantId, userId)
                .orElseGet(Rating::new);

        rating.setRestaurant(restaurant);
        rating.setUser(user);
        rating.setRating(request.getRating());
        rating.setReview(request.getReview());

        ratingRepository.save(rating);
        return new ResponseEntity<>("Rating added successfully", HttpStatus.CREATED);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Double> getAverageRating(Long restaurantId) {
        return new ResponseEntity<>(ratingRepository.findAverageRating(restaurantId), HttpStatus.OK);
    }
}
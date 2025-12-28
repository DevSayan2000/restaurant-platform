package com.example.restaurantplatform.repository;

import com.example.restaurantplatform.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    Optional<Rating> findByRestaurantIdAndUserId(Long restaurantId, Long userId);

    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.restaurant.id = :restaurantId")
    Double findAverageRating(Long restaurantId);
}
package com.example.restaurantplatform.repository;

import com.example.restaurantplatform.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.restaurant.id = :restaurantId")
    Double findAverageRating(Long restaurantId);
}
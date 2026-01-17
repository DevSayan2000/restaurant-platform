package com.example.restaurantplatform.repository;

import com.example.restaurantplatform.entity.Restaurant;
import com.example.restaurantplatform.entity.User;
import com.example.restaurantplatform.mapper.ReviewMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("""
        SELECT new com.example.restaurantplatform.mapper.ReviewMapper(
            r.restaurant.name,
            r.review,
            r.rating,
            r.createdAt
        )
        FROM Rating r
        WHERE r.user.email = :userEmail
        AND r.review IS NOT NULL
        ORDER BY r.createdAt DESC
    """)
    List<ReviewMapper> findAllReviewsGivenByUser(@Param("userEmail") String userEmail );

    @Query("""
           SELECT DISTINCT r
           FROM Rating rt
           JOIN rt.restaurant r
           JOIN rt.user u
           WHERE u.email = :userEmail
           AND rt.review IS NOT NULL
           """)
    List<Restaurant> findRestaurantsReviewedByUser(@Param("userEmail") String userEmail);
}
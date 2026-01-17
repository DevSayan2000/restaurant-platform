package com.example.restaurantplatform.repository;

import com.example.restaurantplatform.entity.Rating;
import com.example.restaurantplatform.mapper.PopularRestaurantMapper;
import com.example.restaurantplatform.mapper.RecentReviewMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface AnalyticsRepository extends JpaRepository<Rating, Long> {

    // ADMIN
    @Query("""
        SELECT COUNT(r), AVG(r.rating)
        FROM Rating r
        WHERE r.restaurant.email = :adminEmail
    """)
    Object[] getRestaurantAdminReviewStats(@Param("adminEmail") String adminEmail);

    @Query("""
        SELECT COUNT(res)
        FROM Restaurant res
        WHERE res.email = :adminEmail
    """)
    Integer getRestaurantAdminRestaurantsCount(@Param("adminEmail") String adminEmail);

    // USER
    @Query("""
        SELECT COUNT(r), AVG(r.rating), COUNT(DISTINCT r.restaurant.id)
        FROM Rating r
        WHERE r.user.email = :userEmail
    """)
    Object[] getUserAnalytics(@Param("userEmail") String userEmail);

    @Query("""
        SELECT new com.example.restaurantplatform.mapper.PopularRestaurantMapper(
            r.restaurant.id,
            r.restaurant.name,
            r.restaurant.city,
            r.restaurant.foodType,
            r.restaurant.cuisine,
            AVG(r.rating)
        )
        FROM Rating r
        GROUP BY r.restaurant.id, r.restaurant.name, r.restaurant.city,
                 r.restaurant.foodType, r.restaurant.cuisine
        ORDER BY AVG(r.rating) DESC, r.restaurant.id ASC
    """)
    List<PopularRestaurantMapper> findTop3PopularRestaurantsForUser(Pageable pageable);

    @Query("""
        SELECT new com.example.restaurantplatform.mapper.RecentReviewMapper(
            r.review,
            r.user.name,
            r.createdAt
        )
        FROM Rating r
        WHERE r.restaurant.email = :adminEmail
        AND r.review IS NOT NULL
        ORDER BY r.createdAt DESC
    """)
    List<RecentReviewMapper> findTop3RecentReviewsForRestaurantAdmin(
            @Param("adminEmail") String adminEmail,
            Pageable pageable
    );
}
package com.example.restaurantplatform.service.impl;

import com.example.restaurantplatform.dto.analytics.AnalyticsResponse;
import com.example.restaurantplatform.dto.analytics.PopularRestaurants;
import com.example.restaurantplatform.dto.analytics.PopularReviews;
import com.example.restaurantplatform.exception.ErrorCode;
import com.example.restaurantplatform.exception.ErrorMessage;
import com.example.restaurantplatform.exception.RestaurantPlatformException;
import com.example.restaurantplatform.mapper.PopularRestaurantMapper;
import com.example.restaurantplatform.mapper.PopularReviewMapper;
import com.example.restaurantplatform.repository.AnalyticsRepository;
import com.example.restaurantplatform.repository.RestaurantRepository;
import com.example.restaurantplatform.repository.UserRepository;
import com.example.restaurantplatform.service.interfaces.AnalyticsService;
import com.example.restaurantplatform.util.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final AnalyticsRepository analyticsRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final CommonUtils commonUtils;

    public ResponseEntity<AnalyticsResponse> getAnalytics() {

        Map<String, String> authDetails = commonUtils.getEmailAndRoleFromAuthToken();

        String email = authDetails.get("email");
        String role = authDetails.get("role");

        if ("ROLE_SUPER_ADMIN".equals(role)) {
            return getSuperAdminAnalytics();
        }

        if ("ROLE_RESTAURANT_ADMIN".equals(role)) {
            return getRestaurantAdminAnalytics(email);
        }

        if ("ROLE_USER".equals(role)) {
            return getUserAnalytics(email);
        }

        throw new RestaurantPlatformException(ErrorCode.INVALID_ROLE, ErrorMessage.INVALID_ROLE);
    }

    public ResponseEntity<PopularRestaurants> getPopularRestaurants() {

        Pageable pageable = PageRequest.of(0, 3);
        List<PopularRestaurantMapper> restaurants = analyticsRepository.findTop3PopularRestaurantsForUser(pageable);

        return new ResponseEntity<>(new PopularRestaurants(restaurants), HttpStatus.OK);
    }

    public ResponseEntity<PopularReviews> getPopularReviews() {

        Map<String, String> auth = commonUtils.getEmailAndRoleFromAuthToken();
        Pageable pageable = PageRequest.of(0, 3);
        List<PopularReviewMapper> reviews = analyticsRepository.findTop3RecentReviewsForRestaurantAdmin(auth.get("email"), pageable);

        return new ResponseEntity<>(new PopularReviews(reviews), HttpStatus.OK);
    }

    private ResponseEntity<AnalyticsResponse> getSuperAdminAnalytics(){
        Integer restaurantCount = restaurantRepository.findAll().size();
        Integer userCount = userRepository.findAll().size();
        return  new ResponseEntity<>(new AnalyticsResponse(
                null,
                null,
                userCount,
                restaurantCount
        ), HttpStatus.OK);
    }

    private ResponseEntity<AnalyticsResponse> getRestaurantAdminAnalytics(String adminEmail) {

        Object[] raw = analyticsRepository.getRestaurantAdminReviewStats(adminEmail);
        Integer restaurantCount = analyticsRepository.getRestaurantAdminRestaurantsCount(adminEmail);

        Object[] reviewStats = (Object[]) raw[0];

        long reviews = reviewStats[0] != null
                ? ((Number) reviewStats[0]).longValue()
                : 0L;

        double avgRating = reviewStats[1] != null
                ? BigDecimal.valueOf(((Number) reviewStats[1]).doubleValue())
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue()
                : 0.0;

        return new ResponseEntity<>(new AnalyticsResponse(
                (int) reviews,
                avgRating,
                null,
                restaurantCount != null ? restaurantCount : 0
        ), HttpStatus.OK);
    }

    private ResponseEntity<AnalyticsResponse> getUserAnalytics(String userEmail) {

        Object[] raw = analyticsRepository.getUserAnalytics(userEmail);
        Object[] stats = (Object[]) raw[0];

        long reviews = stats[0] != null
                ? ((Number) stats[0]).longValue()
                : 0L;

        double avgRating = stats[1] != null
                ? BigDecimal.valueOf(((Number) stats[1]).doubleValue())
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue()
                : 0.0;

        long restaurants = stats[2] != null
                ? ((Number) stats[2]).longValue()
                : 0L;

        return new ResponseEntity<>(new AnalyticsResponse(
                (int) reviews,
                avgRating,
                null,
                (int) restaurants
        ), HttpStatus.OK);
    }
}
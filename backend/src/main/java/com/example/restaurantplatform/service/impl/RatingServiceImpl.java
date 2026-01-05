package com.example.restaurantplatform.service.impl;

import com.example.restaurantplatform.dto.general.GenericResponse;
import com.example.restaurantplatform.dto.rating.AllReviewsResponse;
import com.example.restaurantplatform.dto.rating.AverageRatingResponse;
import com.example.restaurantplatform.dto.rating.CreateRatingRequest;
import com.example.restaurantplatform.entity.Rating;
import com.example.restaurantplatform.entity.Restaurant;
import com.example.restaurantplatform.entity.User;
import com.example.restaurantplatform.enums.Role;
import com.example.restaurantplatform.exception.ErrorCode;
import com.example.restaurantplatform.exception.ErrorMessage;
import com.example.restaurantplatform.exception.RestaurantPlatformException;
import com.example.restaurantplatform.repository.RatingRepository;
import com.example.restaurantplatform.repository.RestaurantRepository;
import com.example.restaurantplatform.repository.UserRepository;
import com.example.restaurantplatform.service.interfaces.RatingService;
import com.example.restaurantplatform.util.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final CommonUtils commonUtils;

    public ResponseEntity<GenericResponse> addOrUpdateRating(Long restaurantId, CreateRatingRequest request) {

        String email = commonUtils.getEmailAndRoleFromAuthToken().get("email");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RestaurantPlatformException(ErrorCode.USER_NOT_FOUND, ErrorMessage.USER_NOT_FOUND));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantPlatformException(ErrorCode.RESTAURANT_NOT_FOUND, ErrorMessage.RESTAURANT_NOT_FOUND));

        Rating rating = ratingRepository
                .findByRestaurantIdAndUserId(restaurantId, user.getId())
                .orElseGet(Rating::new);

        if (rating.getRating() == null || rating.getRating() == 0) {
            rating.setRestaurant(restaurant);
            rating.setUser(user);
            rating.setRating(request.getRating());
            rating.setReview(request.getReview());
            ratingRepository.save(rating);
            GenericResponse response = new GenericResponse("Rating/Review added successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        if (request.getRating() != null) {
            rating.setRating(request.getRating());
        }
        if (request.getReview() != null &&  !request.getReview().isEmpty()) {
            rating.setReview(request.getReview());
        }
        ratingRepository.save(rating);
        GenericResponse response = new GenericResponse("Rating/Review updated successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<AverageRatingResponse> getAverageRating(Long restaurantId) {
        verifyRestaurantExistsAndEmailIdAccess(restaurantId);
        double avgRating = ratingRepository.findAverageRating(restaurantId);
        AverageRatingResponse avgRatingResponse = new AverageRatingResponse(avgRating);
        return new ResponseEntity<>(avgRatingResponse, HttpStatus.OK);
    }

    public ResponseEntity<AllReviewsResponse> getAllReviews(Long restaurantId) {
        verifyRestaurantExistsAndEmailIdAccess(restaurantId);
        List<String> allReviews = ratingRepository.findAllReviewsByRestaurantId(restaurantId);
        AllReviewsResponse allReviewsResponse = new AllReviewsResponse(allReviews);
        return new ResponseEntity<>(allReviewsResponse, HttpStatus.OK);
    }

    private void verifyRestaurantExistsAndEmailIdAccess(Long restaurantId) {
        String email = commonUtils.getEmailAndRoleFromAuthToken().get("email");
        String role = commonUtils.getEmailAndRoleFromAuthToken().get("role");
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
        if (restaurant == null){
            throw new RestaurantPlatformException(ErrorCode.RESTAURANT_NOT_FOUND, ErrorMessage.RESTAURANT_NOT_FOUND);
        }
        if (role.equals(Role.ROLE_RESTAURANT_ADMIN.name()) && !restaurant.getEmail().equals(email)) {
            throw new RestaurantPlatformException(ErrorCode.UNAUTHORIZED, ErrorMessage.UNAUTHORIZED);
        }
    }
}
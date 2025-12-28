package com.example.restaurantplatform.service.impl;

import com.example.restaurantplatform.dto.CreateRestaurantRequest;
import com.example.restaurantplatform.dto.RestaurantResponse;
import com.example.restaurantplatform.entity.Restaurant;
import com.example.restaurantplatform.repository.FootfallRepository;
import com.example.restaurantplatform.repository.RatingRepository;
import com.example.restaurantplatform.repository.RestaurantRepository;
import com.example.restaurantplatform.service.RestaurantService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RatingRepository ratingRepository;
    private final FootfallRepository footfallRepository;

    public ResponseEntity<String> createRestaurant(CreateRestaurantRequest request) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.getName());
        restaurant.setCity(request.getCity());
        restaurant.setArea(request.getArea());
        restaurant.setCuisine(request.getCuisine());

        restaurantRepository.save(restaurant);
        return new ResponseEntity<>("Restaurant created successfully", HttpStatus.CREATED);
    }

    public ResponseEntity<List<RestaurantResponse>> getRestaurants() {
        return new ResponseEntity<>(restaurantRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList(), HttpStatus.OK);
    }

    public ResponseEntity<List<RestaurantResponse>> getRestaurantsByCity(String city) {
        return new ResponseEntity<>(restaurantRepository.findByCity(city)
                .stream()
                .map(this::toResponse)
                .toList(), HttpStatus.OK);
    }

    private RestaurantResponse toResponse(Restaurant restaurant) {
        Double avgRating = ratingRepository.findAverageRating(restaurant.getId());
        Double avgFootfall = footfallRepository.findAverageFootfall(restaurant.getId());

        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getCity(),
                restaurant.getCuisine(),
                avgRating,
                avgFootfall
        );
    }
}


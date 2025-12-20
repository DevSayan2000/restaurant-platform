package com.example.restaurantplatform.service;

import com.example.restaurantplatform.dto.CreateRestaurantRequest;
import com.example.restaurantplatform.dto.RestaurantResponse;
import com.example.restaurantplatform.entity.Restaurant;
import com.example.restaurantplatform.repository.FootfallRepository;
import com.example.restaurantplatform.repository.RatingRepository;
import com.example.restaurantplatform.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RatingRepository ratingRepository;
    private final FootfallRepository footfallRepository;

    public void createRestaurant(CreateRestaurantRequest request) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.getName());
        restaurant.setCity(request.getCity());
        restaurant.setArea(request.getArea());
        restaurant.setCuisine(request.getCuisine());

        restaurantRepository.save(restaurant);
    }

    public List<RestaurantResponse> getRestaurantsByCity(String city) {
        return restaurantRepository.findByCity(city)
                .stream()
                .map(this::toResponse)
                .toList();
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


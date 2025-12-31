package com.example.restaurantplatform.service.impl;

import com.example.restaurantplatform.dto.restaurant.CreateRestaurantRequest;
import com.example.restaurantplatform.dto.restaurant.RestaurantResponse;
import com.example.restaurantplatform.entity.Restaurant;
import com.example.restaurantplatform.enums.Role;
import com.example.restaurantplatform.repository.RatingRepository;
import com.example.restaurantplatform.repository.RestaurantRepository;
import com.example.restaurantplatform.service.interfaces.RestaurantService;
import com.example.restaurantplatform.util.CommonUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RatingRepository ratingRepository;
    private final CommonUtils commonUtils;

    public ResponseEntity<String> createRestaurant(CreateRestaurantRequest request) {
        String email = commonUtils.getEmailAndRoleFromAuthToken().get("email");

        Restaurant restaurant = restaurantRepository.findByEmailAndName(email, request.getName()).orElse(null);
        if (restaurant != null){
            return new ResponseEntity<>("Restaurant already exists", HttpStatus.CONFLICT);
        }

        restaurant = new Restaurant();
        restaurant.setEmail(email);
        restaurant.setName(request.getName());
        restaurant.setCity(request.getCity());
        restaurant.setFoodType(request.getFoodType());
        restaurant.setCuisine(request.getCuisine());

        restaurantRepository.save(restaurant);
        return new ResponseEntity<>("Restaurant created successfully", HttpStatus.CREATED);
    }

    public ResponseEntity<String> deleteRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
        if (restaurant == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found");
        }
        String email =  commonUtils.getEmailAndRoleFromAuthToken().get("email");
        if (!restaurant.getEmail().equals(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Restaurant not allowed to be deleted");
        }
        restaurantRepository.delete(restaurant);
        return new ResponseEntity<>("Restaurant deleted successfully", HttpStatus.OK);
    }

    public ResponseEntity<List<RestaurantResponse>> getRestaurants() {
        Map<String, String> emailAndRole = commonUtils.getEmailAndRoleFromAuthToken();
        String role = emailAndRole.get("role");
        if (role.equals(Role.ROLE_RESTAURANT_ADMIN.name())) {
            String email = emailAndRole.get("email");
            return new ResponseEntity<>(restaurantRepository.findByEmail(email)
                    .stream()
                    .sorted(Comparator.comparingLong(Restaurant::getId))
                    .map(this::toResponse)
                    .toList(), HttpStatus.OK);
        }
        return new ResponseEntity<>(restaurantRepository.findAll()
                .stream()
                .sorted(Comparator.comparingLong(Restaurant::getId))
                .map(this::toResponse)
                .toList(), HttpStatus.OK);
    }

    public ResponseEntity<List<RestaurantResponse>> getRestaurantsByCity(String city) {
        Map<String, String> emailAndRole = commonUtils.getEmailAndRoleFromAuthToken();
        String role = emailAndRole.get("role");
        if (role.equals(Role.ROLE_RESTAURANT_ADMIN.name())) {
            String email = emailAndRole.get("email");
            return new ResponseEntity<>(restaurantRepository.findByEmailAndCity(email, city)
                    .stream()
                    .sorted(Comparator.comparingLong(Restaurant::getId))
                    .map(this::toResponse)
                    .toList(), HttpStatus.OK);
        }
        return new ResponseEntity<>(restaurantRepository.findByCity(city)
                .stream()
                .sorted(Comparator.comparingLong(Restaurant::getId))
                .map(this::toResponse)
                .toList(), HttpStatus.OK);
    }

    private RestaurantResponse toResponse(Restaurant restaurant) {
        Double avgRating = ratingRepository.findAverageRating(restaurant.getId());

        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getCity(),
                restaurant.getFoodType(),
                restaurant.getCuisine(),
                avgRating
        );
    }
}


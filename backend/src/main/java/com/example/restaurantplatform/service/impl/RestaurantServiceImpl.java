package com.example.restaurantplatform.service.impl;

import com.example.restaurantplatform.dto.general.GenericResponse;
import com.example.restaurantplatform.dto.restaurant.CreateRestaurantRequest;
import com.example.restaurantplatform.dto.restaurant.ListRestaurantResponse;
import com.example.restaurantplatform.dto.restaurant.RestaurantResponse;
import com.example.restaurantplatform.dto.restaurant.UpdateRestaurantRequest;
import com.example.restaurantplatform.entity.Restaurant;
import com.example.restaurantplatform.enums.Role;
import com.example.restaurantplatform.exception.ErrorCode;
import com.example.restaurantplatform.exception.ErrorMessage;
import com.example.restaurantplatform.exception.RestaurantPlatformException;
import com.example.restaurantplatform.repository.RatingRepository;
import com.example.restaurantplatform.repository.RestaurantRepository;
import com.example.restaurantplatform.service.interfaces.RestaurantService;
import com.example.restaurantplatform.util.CommonUtils;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RatingRepository ratingRepository;
    private final CommonUtils commonUtils;

    @Transactional
    public ResponseEntity<GenericResponse> createRestaurant(CreateRestaurantRequest request) {
        String email = commonUtils.getEmailAndRoleFromAuthToken().get("email");

        Restaurant restaurant = restaurantRepository.findByEmailAndName(email, request.getName()).orElse(null);
        if (restaurant != null) {
            throw new RestaurantPlatformException(ErrorCode.RESTAURANT_ALREADY_EXISTS, ErrorMessage.RESTAURANT_ALREADY_EXISTS_EMAIL, email);
        }

        Restaurant res = restaurantRepository.findByNameAndCity(request.getName(), request.getCity()).orElse(null);
        if (res != null) {
            throw new RestaurantPlatformException(ErrorCode.RESTAURANT_ALREADY_EXISTS, ErrorMessage.RESTAURANT_ALREADY_EXISTS_CITY, request.getCity());
        }

        restaurant = new Restaurant();
        restaurant.setEmail(email);
        restaurant.setName(request.getName());
        restaurant.setCity(request.getCity());
        restaurant.setFoodType(request.getFoodType());
        restaurant.setCuisine(request.getCuisine());

        Restaurant saved = restaurantRepository.save(restaurant);
        GenericResponse genericResponse = new GenericResponse("Restaurant created successfully", saved.getId());
        return new ResponseEntity<>(genericResponse, HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<GenericResponse> updateRestaurant(Long restaurantId, UpdateRestaurantRequest request) {

        if (restaurantId == null) {
            throw new RestaurantPlatformException(ErrorCode.PARAMETER_NOT_NULL, ErrorMessage.PARAMETER_NOT_NULL, "restaurantId");
        }

        if ((request.getName() == null || request.getName().isBlank()) && (request.getCity() == null || request.getCity().isBlank())
                && (request.getFoodType() == null || request.getFoodType().name().isBlank()) && (request.getCuisine() == null || request.getCuisine().isBlank()))
        {
            throw new RestaurantPlatformException(ErrorCode.NOTHING_TO_UPDATE, ErrorMessage.NOTHING_TO_UPDATE);
        }

        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
        if (restaurant == null) {
            throw new RestaurantPlatformException(ErrorCode.RESTAURANT_NOT_FOUND, ErrorMessage.RESTAURANT_NOT_FOUND);
        }

        String email =  commonUtils.getEmailAndRoleFromAuthToken().get("email");
        if (!restaurant.getEmail().equals(email)) {
            throw new RestaurantPlatformException(ErrorCode.FORBIDDEN, ErrorMessage.RESTAURANT_NOT_ALLOWED_TO_BE_UPDATED);
        }

        if (request.getName() != null && !request.getName().isBlank())
        {
            restaurant.setName(request.getName());
        }

        if (request.getCity() != null && !request.getCity().isBlank())
        {
            restaurant.setCity(request.getCity());
        }

        if (request.getFoodType() != null && !request.getFoodType().name().isBlank())
        {
            restaurant.setFoodType(request.getFoodType());
        }

        if (request.getCuisine() != null && !request.getCuisine().isBlank())
        {
            restaurant.setCuisine(request.getCuisine());
        }

        restaurantRepository.save(restaurant);
        GenericResponse genericResponse = new GenericResponse("Restaurant updated successfully");
        return new ResponseEntity<>(genericResponse, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<GenericResponse> deleteRestaurant(Long restaurantId) {
        if (restaurantId == null){
            throw new RestaurantPlatformException(ErrorCode.PARAMETER_NOT_NULL, ErrorMessage.PARAMETER_NOT_NULL, "restaurantId");
        }
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
        if (restaurant == null) {
            throw new RestaurantPlatformException(ErrorCode.RESTAURANT_NOT_FOUND, ErrorMessage.RESTAURANT_NOT_FOUND);
        }

        String email =  commonUtils.getEmailAndRoleFromAuthToken().get("email");
        if (!restaurant.getEmail().equals(email)) {
            throw new RestaurantPlatformException(ErrorCode.FORBIDDEN, ErrorMessage.RESTAURANT_NOT_ALLOWED_TO_BE_DELETED);
        }

        restaurantRepository.delete(restaurant);
        GenericResponse genericResponse = new GenericResponse("Restaurant deleted successfully");
        return new ResponseEntity<>(genericResponse, HttpStatus.OK);
    }

    public ResponseEntity<ListRestaurantResponse> getRestaurants() {
        Map<String, String> emailAndRole = commonUtils.getEmailAndRoleFromAuthToken();
        String role = emailAndRole.get("role");
        if (role.equals(Role.ROLE_RESTAURANT_ADMIN.name())) {
            String email = emailAndRole.get("email");
            return new ResponseEntity<>(new ListRestaurantResponse(restaurantRepository.findByEmail(email)
                    .stream()
                    .sorted(Comparator.comparingLong(Restaurant::getId))
                    .map(this::toResponse)
                    .toList()), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ListRestaurantResponse(restaurantRepository.findAll()
                .stream()
                .sorted(Comparator.comparingLong(Restaurant::getId))
                .map(this::toResponseForSA)
                .toList()), HttpStatus.OK);
    }

    public ResponseEntity<ListRestaurantResponse> getRestaurantsByCity(String city) {
        Map<String, String> emailAndRole = commonUtils.getEmailAndRoleFromAuthToken();
        String role = emailAndRole.get("role");
        if (role.equals(Role.ROLE_RESTAURANT_ADMIN.name())) {
            String email = emailAndRole.get("email");
            return new ResponseEntity<>(new ListRestaurantResponse(restaurantRepository.findByEmailAndCity(email, city)
                    .stream()
                    .sorted(Comparator.comparingLong(Restaurant::getId))
                    .map(this::toResponse)
                    .toList()), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ListRestaurantResponse(restaurantRepository.findByCity(city)
                .stream()
                .sorted(Comparator.comparingLong(Restaurant::getId))
                .map(this::toResponseForSA)
                .toList()), HttpStatus.OK);
    }

    public ResponseEntity<RestaurantResponse> getRestaurantById(Long restaurantId) {
        if (restaurantId == null){
            throw new RestaurantPlatformException(ErrorCode.PARAMETER_NOT_NULL, ErrorMessage.PARAMETER_NOT_NULL, "restaurantId");
        }
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
        if (restaurant == null) {
            throw new RestaurantPlatformException(ErrorCode.RESTAURANT_NOT_FOUND, ErrorMessage.RESTAURANT_NOT_FOUND);
        }

        Map<String, String> emailAndRole = commonUtils.getEmailAndRoleFromAuthToken();
        String role = emailAndRole.get("role");
        String email = emailAndRole.get("email");
        
        if (role.equals(Role.ROLE_RESTAURANT_ADMIN.name()) && !restaurant.getEmail().equals(email)) {
            throw new RestaurantPlatformException(ErrorCode.FORBIDDEN, ErrorMessage.UNAUTHORIZED);
        }

        RestaurantResponse response = role.equals(Role.ROLE_RESTAURANT_ADMIN.name())
            ? toResponse(restaurant)
            : toResponseForSA(restaurant);

        // Determine ownership: compare token email with restaurant creator email from DB
        boolean isOwner = role.equals(Role.ROLE_RESTAURANT_ADMIN.name())
            && restaurant.getEmail().equals(email);
        response.setOwner(isOwner);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private RestaurantResponse toResponse(Restaurant restaurant) {
        Double avgRating = ratingRepository.findAverageRating(restaurant.getId());

        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getCity(),
                restaurant.getFoodType(),
                restaurant.getCuisine(),
                avgRating,
                null,
                restaurant.getCreatedAt()
        );
    }

    private RestaurantResponse toResponseForSA(Restaurant restaurant) {
        Double avgRating = ratingRepository.findAverageRating(restaurant.getId());

        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getCity(),
                restaurant.getFoodType(),
                restaurant.getCuisine(),
                avgRating,
                restaurant.getEmail(),
                restaurant.getCreatedAt()
        );
    }
}


package com.example.restaurantplatform.service.impl;

import com.example.restaurantplatform.dto.general.GenericResponse;
import com.example.restaurantplatform.dto.restaurant.RestaurantResponse;
import com.example.restaurantplatform.dto.user.CreateUserRequest;
import com.example.restaurantplatform.dto.user.UserResponse;
import com.example.restaurantplatform.entity.Restaurant;
import com.example.restaurantplatform.entity.User;
import com.example.restaurantplatform.enums.Role;
import com.example.restaurantplatform.exception.ErrorCode;
import com.example.restaurantplatform.exception.ErrorMessage;
import com.example.restaurantplatform.exception.RestaurantPlatformException;
import com.example.restaurantplatform.repository.RatingRepository;
import com.example.restaurantplatform.repository.RestaurantRepository;
import com.example.restaurantplatform.repository.UserRepository;
import com.example.restaurantplatform.service.interfaces.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;
    private final RestaurantRepository restaurantRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<GenericResponse> createUser(CreateUserRequest request) {

        Role requestedRole = request.getRole();
        if (requestedRole != Role.ROLE_USER && requestedRole != Role.ROLE_RESTAURANT_ADMIN) {
            throw new RestaurantPlatformException(ErrorCode.INVALID_ROLE, ErrorMessage.INVALID_ROLE);
        }

        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (user != null) {
            throw new RestaurantPlatformException(ErrorCode.USER_ALREADY_EXISTS, ErrorMessage.USER_ALREADY_EXISTS);
        }

        user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setPassword(passwordEncoder.encode(request.getPassword()));


        userRepository.save(user);
        GenericResponse genericResponse = new GenericResponse("User created successfully");
        return new ResponseEntity<>(genericResponse, HttpStatus.CREATED);
    }

    public ResponseEntity<GenericResponse> deleteUser(Long userId) {
        if (userId == null){
            throw new RestaurantPlatformException(ErrorCode.PARAMETER_NOT_NULL, ErrorMessage.PARAMETER_NOT_NULL, "userId");
        }
        User user =  userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new RestaurantPlatformException(ErrorCode.USER_NOT_FOUND, ErrorMessage.USER_NOT_FOUND);
        }
        userRepository.delete(user);
        GenericResponse genericResponse = new GenericResponse("User deleted successfully");
        return new ResponseEntity<>(genericResponse, HttpStatus.OK);
    }

    public ResponseEntity<List<UserResponse>> getUsers() {
        return new ResponseEntity<>(
                userRepository.findAll()
                        .stream()
                        .sorted(Comparator.comparingLong(User::getId))
                        .map(this::toResponse)
                        .toList(),
                HttpStatus.OK
        );
    }

    public ResponseEntity<List<RestaurantResponse>> getAllRestaurantsForUsers() {
        return new ResponseEntity<>(restaurantRepository.findAll()
                .stream()
                .sorted(Comparator.comparingLong(Restaurant::getId))
                .map(this::toResponse)
                .toList(), HttpStatus.OK);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
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
                null
        );
    }
}


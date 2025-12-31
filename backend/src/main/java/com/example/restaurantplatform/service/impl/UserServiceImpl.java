package com.example.restaurantplatform.service.impl;

import com.example.restaurantplatform.dto.restaurant.RestaurantResponse;
import com.example.restaurantplatform.dto.user.CreateUserRequest;
import com.example.restaurantplatform.dto.user.UserResponse;
import com.example.restaurantplatform.entity.Restaurant;
import com.example.restaurantplatform.entity.User;
import com.example.restaurantplatform.enums.Role;
import com.example.restaurantplatform.repository.RatingRepository;
import com.example.restaurantplatform.repository.RestaurantRepository;
import com.example.restaurantplatform.repository.UserRepository;
import com.example.restaurantplatform.service.interfaces.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public ResponseEntity<String> createUser(CreateUserRequest request) {

        Role requestedRole = request.getRole();
        if (requestedRole != Role.ROLE_USER &&
                requestedRole != Role.ROLE_RESTAURANT_ADMIN) {
            throw new AccessDeniedException("Invalid role");
        }

        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (user != null) {
            throw new AccessDeniedException("User already exists");
        }

        user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setPassword(passwordEncoder.encode(request.getPassword()));


        userRepository.save(user);
        return new ResponseEntity<>("User created successfully", HttpStatus.CREATED);
    }

    public ResponseEntity<String> deleteUser(Long userId) {
        User user =  userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        userRepository.delete(user);
        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
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

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public ResponseEntity<List<RestaurantResponse>> getAllRestaurantsForUsers() {
        return new ResponseEntity<>(restaurantRepository.findAll()
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


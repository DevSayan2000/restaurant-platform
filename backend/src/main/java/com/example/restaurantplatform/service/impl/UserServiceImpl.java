package com.example.restaurantplatform.service.impl;

import com.example.restaurantplatform.dto.general.GenericResponse;
import com.example.restaurantplatform.dto.restaurant.ListRestaurantResponse;
import com.example.restaurantplatform.dto.restaurant.RestaurantResponse;
import com.example.restaurantplatform.dto.user.*;
import com.example.restaurantplatform.entity.Restaurant;
import com.example.restaurantplatform.entity.User;
import com.example.restaurantplatform.enums.Role;
import com.example.restaurantplatform.exception.ErrorCode;
import com.example.restaurantplatform.exception.ErrorMessage;
import com.example.restaurantplatform.exception.RestaurantPlatformException;
import com.example.restaurantplatform.mapper.ReviewMapper;
import com.example.restaurantplatform.repository.RatingRepository;
import com.example.restaurantplatform.repository.RestaurantRepository;
import com.example.restaurantplatform.repository.UserRepository;
import com.example.restaurantplatform.service.interfaces.UserService;
import com.example.restaurantplatform.util.CommonUtils;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;
    private final RestaurantRepository restaurantRepository;
    private final PasswordEncoder passwordEncoder;
    private final CommonUtils commonUtils;

    @Transactional
    public ResponseEntity<GenericResponse> createUser(CreateUserRequest request) {

        Role requestedRole = request.getRole();
        if (requestedRole != Role.ROLE_USER && requestedRole != Role.ROLE_RESTAURANT_ADMIN) {
            throw new RestaurantPlatformException(ErrorCode.INVALID_ROLE, ErrorMessage.INVALID_ROLE);
        }

        userRepository.findByEmail(request.getEmail())
                .ifPresent(u -> {
                    throw new RestaurantPlatformException(
                            ErrorCode.USER_ALREADY_EXISTS,
                            ErrorMessage.USER_ALREADY_EXISTS
                    );
                });

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setPassword(passwordEncoder.encode(request.getPassword()));


        userRepository.save(user);
        GenericResponse genericResponse = new GenericResponse("User created successfully");
        return new ResponseEntity<>(genericResponse, HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<GenericResponse> updateUser(UpdateUserRequest request) {

        if ((request.getName() == null || request.getName().isBlank()) &&
                (request.getNewPassword() == null ||  request.getNewPassword().isBlank())) {
            throw new RestaurantPlatformException(ErrorCode.NOTHING_TO_UPDATE, ErrorMessage.NOTHING_TO_UPDATE);
        }

        Map<String, String> emailAndRole = commonUtils.getEmailAndRoleFromAuthToken();
        String email = emailAndRole.get("email");
        User user = userRepository
                .findByEmail(email).orElseThrow(() ->
                        new RestaurantPlatformException(ErrorCode.USER_NOT_FOUND, ErrorMessage.USER_NOT_FOUND));

        if (request.getName() != null &&  !request.getName().isBlank()) {
            user.setName(request.getName());
        }
        if (request.getNewPassword() != null && !request.getNewPassword().isBlank()) {
            if (request.getCurrentPassword() == null || request.getCurrentPassword().isBlank()) {
                throw new RestaurantPlatformException(
                        ErrorCode.CURRENT_PASSWORD_REQUIRED,
                        ErrorMessage.CURRENT_PASSWORD_REQUIRED
                );
            }

            if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
                throw new RestaurantPlatformException(
                        ErrorCode.CURRENT_PASSWORD_INCORRECT,
                        ErrorMessage.CURRENT_PASSWORD_INCORRECT
                );
            }

            if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
                throw new RestaurantPlatformException(
                        ErrorCode.NEW_PASSWORD_SAME_AS_CURRENT,
                        ErrorMessage.NEW_PASSWORD_SAME_AS_CURRENT
                );
            }

            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }

        userRepository.save(user);
        GenericResponse genericResponse = new GenericResponse("User updated successfully");
        return new ResponseEntity<>(genericResponse, HttpStatus.OK);
    }

    @Transactional
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

    public ResponseEntity<ListUserResponse> getUsers() {
        return new ResponseEntity<>(new ListUserResponse(
                userRepository.findAll()
                        .stream()
                        .sorted(Comparator.comparingLong(User::getId))
                        .map(this::toResponse)
                        .toList()),
                HttpStatus.OK
        );
    }

    public ResponseEntity<ListRestaurantResponse> getAllRestaurantsForUsers() {
        return new ResponseEntity<>(new ListRestaurantResponse(restaurantRepository.findAll()
                .stream()
                .sorted(Comparator.comparingLong(Restaurant::getId))
                .map(this::toResponse)
                .toList()), HttpStatus.OK);
    }

    public  ResponseEntity<Reviews> getAllReviewsGivenByUser() {
        String email = commonUtils.getEmailAndRoleFromAuthToken().get("email");
        List<ReviewMapper> reviews = userRepository.findAllReviewsGivenByUser(email);

        return new ResponseEntity<>(new Reviews(reviews), HttpStatus.OK);
    }

    public  ResponseEntity<ListRestaurantResponse> getRestaurantsReviewedByUser() {
        String email = commonUtils.getEmailAndRoleFromAuthToken().get("email");
        return new ResponseEntity<>(new ListRestaurantResponse(userRepository.findRestaurantsReviewedByUser(email)
                .stream()
                .sorted(Comparator.comparingLong(Restaurant::getId))
                .map(this::toResponse)
                .toList()), HttpStatus.OK);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name()
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


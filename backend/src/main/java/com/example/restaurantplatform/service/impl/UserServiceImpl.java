package com.example.restaurantplatform.service.impl;

import com.example.restaurantplatform.dto.CreateUserRequest;
import com.example.restaurantplatform.dto.UserResponse;
import com.example.restaurantplatform.entity.User;
import com.example.restaurantplatform.repository.UserRepository;
import com.example.restaurantplatform.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public ResponseEntity<String> createUser(CreateUserRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        userRepository.save(user);
        return new ResponseEntity<>("User created successfully", HttpStatus.CREATED);
    }

    public ResponseEntity<List<UserResponse>> getUsers() {
        return new ResponseEntity<>(userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList(), HttpStatus.OK);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }
}


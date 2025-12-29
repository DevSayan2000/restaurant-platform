package com.example.restaurantplatform.service.impl;

import com.example.restaurantplatform.dto.user.CreateUserRequest;
import com.example.restaurantplatform.dto.user.UserResponse;
import com.example.restaurantplatform.entity.User;
import com.example.restaurantplatform.enums.Role;
import com.example.restaurantplatform.repository.UserRepository;
import com.example.restaurantplatform.service.interfaces.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
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

    public ResponseEntity<List<UserResponse>> getUsers() {
        return new ResponseEntity<>(userRepository.findAll()
                .stream()
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
}


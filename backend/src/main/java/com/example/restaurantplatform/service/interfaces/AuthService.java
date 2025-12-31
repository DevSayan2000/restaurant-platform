package com.example.restaurantplatform.service.interfaces;

import com.example.restaurantplatform.dto.auth.AuthRequest;
import com.example.restaurantplatform.dto.auth.AuthResponse;
import com.example.restaurantplatform.dto.auth.UserProfileResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<AuthResponse> login(AuthRequest request);

    ResponseEntity<UserProfileResponse> me();
}

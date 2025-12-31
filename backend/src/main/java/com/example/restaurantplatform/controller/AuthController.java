package com.example.restaurantplatform.controller;

import com.example.restaurantplatform.dto.auth.AuthRequest;
import com.example.restaurantplatform.dto.auth.AuthResponse;
import com.example.restaurantplatform.dto.auth.UserProfileResponse;
import com.example.restaurantplatform.service.interfaces.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Create login",
            description = "Creates login for Restaurant_Admin, User"
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody @Valid AuthRequest request) {
        return authService.login(request);
    }

    @Operation(
            summary = "Get login details",
            description = "Fetches login details like name, email"
    )
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> me() {
        return authService.me();
    }
}
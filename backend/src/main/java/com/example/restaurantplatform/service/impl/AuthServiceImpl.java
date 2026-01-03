package com.example.restaurantplatform.service.impl;

import com.example.restaurantplatform.dto.auth.AuthRequest;
import com.example.restaurantplatform.dto.auth.AuthResponse;
import com.example.restaurantplatform.dto.auth.UserProfileResponse;
import com.example.restaurantplatform.entity.User;
import com.example.restaurantplatform.exception.ErrorCode;
import com.example.restaurantplatform.exception.ErrorMessage;
import com.example.restaurantplatform.exception.RestaurantPlatformException;
import com.example.restaurantplatform.repository.UserRepository;
import com.example.restaurantplatform.security.jwt.JwtService;
import com.example.restaurantplatform.service.interfaces.AuthService;
import com.example.restaurantplatform.util.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final CommonUtils commonUtils;

    public ResponseEntity<AuthResponse> login(AuthRequest request) {

        User user = userRepository
                .findByEmail(request.getEmail()).orElseThrow(() -> new RestaurantPlatformException(ErrorCode.USER_NOT_FOUND, ErrorMessage.USER_NOT_FOUND));

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        }
        catch (Exception e) {
            throw  new RestaurantPlatformException(ErrorCode.EMAIL_PASSWORD_NOT_FOUND, ErrorMessage.EMAIL_PASSWORD_NOT_FOUND);
        }

        String token = jwtService.generateToken(user);

        return new ResponseEntity<>(new AuthResponse(token), HttpStatus.OK);
    }

    public ResponseEntity<UserProfileResponse> me() {

        String email = commonUtils.getEmailAndRoleFromAuthToken().get("email");
        String role = commonUtils.getEmailAndRoleFromAuthToken().get("role");

        User user = userRepository.findByEmail(email).orElseThrow(() -> new RestaurantPlatformException(ErrorCode.USER_NOT_FOUND, ErrorMessage.USER_NOT_FOUND));
        UserProfileResponse userProfileResponse = new UserProfileResponse(user.getId(), user.getName(), user.getEmail(), role);

        return new ResponseEntity<>(userProfileResponse, HttpStatus.OK);
    }
}
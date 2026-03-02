package com.example.restaurantplatform.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String jwt = authHeader.substring(7);
            String email = jwtService.extractUsername(jwt);

            if (email != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                // Validate the token (checks signature + expiry) without hitting the DB
                if (jwtService.isTokenNotExpired(jwt)) {

                    String role = jwtService.extractRole(jwt);

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    email,          // principal = email string
                                    null,           // credentials
                                    List.of(new SimpleGrantedAuthority(role))
                            );

                    SecurityContextHolder.getContext()
                            .setAuthentication(authToken);
                } else {
                    log.warn("JWT token expired for user: {}", email);
                }
            }
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            log.warn("JWT token expired: {}", e.getMessage());
        } catch (io.jsonwebtoken.JwtException e) {
            log.warn("JWT token invalid: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Error during JWT authentication: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
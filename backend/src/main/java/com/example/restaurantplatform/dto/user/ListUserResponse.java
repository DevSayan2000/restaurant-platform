package com.example.restaurantplatform.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ListUserResponse {

    List<UserResponse> userResponses;
}
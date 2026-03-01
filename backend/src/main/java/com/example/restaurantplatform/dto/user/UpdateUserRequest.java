package com.example.restaurantplatform.dto.user;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {
    private String name;
    private String currentPassword;

    @Size(min = 6)
    private String newPassword;
}
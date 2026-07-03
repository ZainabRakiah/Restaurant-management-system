package com.restaurant.common.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank String name,
        @Email String email,
        @NotBlank @Size(min = 6) String password,
        String phone,
        String role
) {
}

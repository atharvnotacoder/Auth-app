package com.lwa.auth_app_backend.Dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        String email,

        String password
) {
}

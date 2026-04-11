package com.lwa.auth_app_backend.Dto;

public record LoginRequest(
        String email,
        String password
) {
}

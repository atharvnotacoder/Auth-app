package com.lwa.auth_app_backend.Dto;

public record ResetPasswordRequest(
        String email,
        String otp,
        String newPassword
) {
}

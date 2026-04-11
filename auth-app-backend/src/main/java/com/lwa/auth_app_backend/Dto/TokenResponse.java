package com.lwa.auth_app_backend.Dto;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        long expiresIn,
        String tokenType,
        UserDto user

) {

    public static TokenResponse of(String accessToken,String refreshToken,long expiresIn,UserDto user ){
        return new TokenResponse(accessToken, refreshToken, expiresIn, "Bearer", user);
    }
}

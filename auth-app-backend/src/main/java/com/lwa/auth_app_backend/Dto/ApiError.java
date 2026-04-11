package com.lwa.auth_app_backend.Dto;

public record ApiError(
        int status,
        String error,
        String message,
        String path
) {
    public static ApiError of(int status,String error,String message, String path){
        return new ApiError(status, error, message, path);
    }
}

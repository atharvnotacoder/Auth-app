package com.lwa.auth_app_backend.Custom_Exceptions;

import com.lwa.auth_app_backend.Dto.ApiError;
import com.lwa.auth_app_backend.Dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    public final Logger logger= LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class, CredentialsExpiredException.class, DisabledException.class}
    )
    public ResponseEntity<ApiError> handeleAuthException(Exception e, HttpServletRequest request){
       logger.info("Exception: {}",e.getClass().getName());
        var apiError= ApiError.of(HttpStatus.BAD_REQUEST.value(),"Bad request",e.getMessage(),request.getRequestURI());
    return ResponseEntity.badRequest().body(apiError);
    }


    //Resource not found exception handling
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleExResourceNotFoundException(ResourceNotFoundException exception){
       ErrorResponse internalServerError= new ErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(internalServerError);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception){
        ErrorResponse internalServerError= new ErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(internalServerError);
    }

}

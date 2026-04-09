package com.lwa.auth_app_backend.Custom_Exceptions;

public class ResourceNotFoundException extends RuntimeException {
   public ResourceNotFoundException(String message){
       super(message);
   }

    public ResourceNotFoundException(){
        super("Resource not found");
    }
}

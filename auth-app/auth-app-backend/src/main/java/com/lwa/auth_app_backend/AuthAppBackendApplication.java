package com.lwa.auth_app_backend;

import com.lwa.auth_app_backend.Entities.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthAppBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthAppBackendApplication.class, args);
		System.out.println("App started..");

	}

}

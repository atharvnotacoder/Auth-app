package com.lwa.auth_app_backend;

import com.lwa.auth_app_backend.Configs.AppConstants;
import com.lwa.auth_app_backend.Entities.Role;
import com.lwa.auth_app_backend.Entities.User;
import com.lwa.auth_app_backend.Repository.RoleRepo;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Base64;
import java.util.UUID;

@SpringBootApplication
public class AuthAppBackendApplication implements CommandLineRunner {

	@Autowired
	private RoleRepo roleRepo;

	public static void main(String[] args) {

		SpringApplication.run(AuthAppBackendApplication.class, args);
		System.out.println("App started..");



	}

	@Override
	public void run(String... args) throws Exception {
		//we will create some default user roles which will be created when app starts
		roleRepo.findByRoleName("ROLE_"+AppConstants.ADMIN_ROLE).ifPresentOrElse(role->{
			System.out.println("Admin role already exist");
		},()->{
			Role role=new Role();
			role.setRoleName("ROLE_"+AppConstants.ADMIN_ROLE);
			role.setId(UUID.randomUUID());
			roleRepo.save(role);
		});

		roleRepo.findByRoleName("ROLE_"+AppConstants.GUEST_ROLE).ifPresentOrElse(role->{
			System.out.println("Guest role already exist");
		},()->{
			Role role=new Role();
			role.setRoleName("ROLE_"+AppConstants.GUEST_ROLE);
			role.setId(UUID.randomUUID());
			roleRepo.save(role);
		});

	}
}

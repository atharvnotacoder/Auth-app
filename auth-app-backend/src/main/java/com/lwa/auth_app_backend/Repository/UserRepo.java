package com.lwa.auth_app_backend.Repository;

import com.lwa.auth_app_backend.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepo extends JpaRepository<User, UUID> {

    public Optional<User> findByEmail(String email);
    public boolean existsByEmail(String email);

}

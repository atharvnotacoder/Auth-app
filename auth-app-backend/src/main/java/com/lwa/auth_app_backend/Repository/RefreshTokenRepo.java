package com.lwa.auth_app_backend.Repository;

import com.lwa.auth_app_backend.Entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken, UUID> {
}

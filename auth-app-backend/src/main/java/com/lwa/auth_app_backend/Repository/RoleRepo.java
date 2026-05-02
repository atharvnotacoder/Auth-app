package com.lwa.auth_app_backend.Repository;

import com.lwa.auth_app_backend.Entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepo extends JpaRepository<Role, UUID> {

    public Optional<Role> findByRoleName(String name);
}

package com.lwa.auth_app_backend.Dto;

import com.lwa.auth_app_backend.Entities.Provider;
import com.lwa.auth_app_backend.Entities.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UserDto {

    private UUID id;
    private String email;
    private String name;
    private String password;
    private String image;
    private boolean enable=true;
    private Instant createdAt=Instant.now();
    private Instant updatedAt=Instant.now();
    private Provider provider=Provider.LOCAL;
    private Set<RoleDto> roles=new HashSet<>();
    private String resetOtp="";
    private long resetOtpExpiresAt=0L;



}

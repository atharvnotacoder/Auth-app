package com.lwa.auth_app_backend.MyAppSecurity;

import com.lwa.auth_app_backend.Custom_Exceptions.ResourceNotFoundException;
import com.lwa.auth_app_backend.Entities.User;
import com.lwa.auth_app_backend.Repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user=userRepo.findByEmail(username).orElseThrow(()->new ResourceNotFoundException("Invalid email or User with this email doesn't exists"));

        return user;
    }
}

package com.lwa.auth_app_backend.Services;

import com.lwa.auth_app_backend.Dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserService userService;
    @Override
    public UserDto registerUser(UserDto userDto) {
        UserDto user = userService.createUser(userDto);
        return user;
    }
}

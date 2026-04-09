package com.lwa.auth_app_backend.Services;

import com.lwa.auth_app_backend.Dto.UserDto;
import com.lwa.auth_app_backend.Entities.User;

public interface AuthService {

    public UserDto registerUser(UserDto userDto);


}

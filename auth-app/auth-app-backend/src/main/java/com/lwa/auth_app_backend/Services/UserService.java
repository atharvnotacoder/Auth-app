package com.lwa.auth_app_backend.Services;

import com.lwa.auth_app_backend.Dto.UserDto;
import com.lwa.auth_app_backend.Entities.User;

import java.util.List;

public interface UserService {
    public UserDto createUser(UserDto userDto);  //Create User

    public UserDto getUserByEmail(String email); //Get User by id

    public UserDto updateUser(UserDto userDto,String userId);  //Update user by email and user

    void deleteUser(String userId); //Delete user by Id

    UserDto getUserById(String userID); //Get User by Id

    Iterable<UserDto> getAllUsers();  //Get a list of all users
}

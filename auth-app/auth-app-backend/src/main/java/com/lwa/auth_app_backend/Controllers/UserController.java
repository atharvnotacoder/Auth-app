package com.lwa.auth_app_backend.Controllers;

import com.lwa.auth_app_backend.Dto.UserDto;
import com.lwa.auth_app_backend.Entities.User;
import com.lwa.auth_app_backend.Services.UserServiceImpl;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    //CREATE A NEW USER API
    @PostMapping

    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userDto));
    }

    //GET ALL USERS API
    @GetMapping
    public ResponseEntity<Iterable<UserDto>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    //GET USER BY EMAIL
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable("email") String email){
        UserDto userByEmail = userService.getUserByEmail(email);
        return ResponseEntity.ok(userByEmail);
    }

    //DELETE USER
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userID") String userID){
        userService.deleteUser(userID);
    }

    //  UPDATE USER API
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("userId") String userId,@RequestBody UserDto userDto){
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(userDto,userId));
    }

    //GET USER BY ID
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("userId") String userId){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserById(userId));
    }
}

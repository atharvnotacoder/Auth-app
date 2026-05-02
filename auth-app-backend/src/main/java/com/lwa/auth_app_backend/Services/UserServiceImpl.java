package com.lwa.auth_app_backend.Services;

import com.lwa.auth_app_backend.Configs.AppConstants;
import com.lwa.auth_app_backend.Custom_Exceptions.ResourceNotFoundException;
import com.lwa.auth_app_backend.Dto.UserDto;
import com.lwa.auth_app_backend.Entities.Provider;
import com.lwa.auth_app_backend.Entities.Role;
import com.lwa.auth_app_backend.Entities.User;
import com.lwa.auth_app_backend.Helpers.UserHelper;
import com.lwa.auth_app_backend.Repository.RoleRepo;
import com.lwa.auth_app_backend.Repository.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{

    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final RoleRepo roleRepo;
    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        if(userDto.getEmail()==null || userDto.getEmail().isBlank()){
            throw new IllegalArgumentException("Email is required");
        }
        if(userRepo.existsByEmail(userDto.getEmail())){
            throw new IllegalArgumentException("user with "+userDto.getEmail()+" exists");
        }

        User user=modelMapper.map(userDto, User.class);

        user.setProvider(userDto.getProvider()!=null?userDto.getProvider(): Provider.LOCAL);

        //Role assignment logic goes here
        //assign default role
        Role role= roleRepo.findByRoleName("ROLE_"+ AppConstants.GUEST_ROLE).orElse(null);
        user.getRoles().add(role);
        User savedUser = userRepo.save(user);

        return modelMapper.map(savedUser,UserDto.class);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with " + email));
        return modelMapper.map(user,UserDto.class);
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        UUID uid = UserHelper.parseUUID(userId);
        User exisitingUser = userRepo.findById(uid).orElseThrow(() -> new ResourceNotFoundException("User does not exist"));
        if(userDto.getName()!=null) exisitingUser.setName(userDto.getName());
        if(userDto.getImage()!=null)exisitingUser.setImage(userDto.getImage());
        if(userDto.getProvider()!=null)exisitingUser.setProvider(userDto.getProvider());
        exisitingUser.setEnable(userDto.isEnable());
        if(userDto.getPassword()!=null)exisitingUser.setPassword(userDto.getPassword());    //TO BE CHANGED LATER

        User updatedUser = userRepo.save(exisitingUser);

        return modelMapper.map(updatedUser,UserDto.class);
    }

    @Override
    public void deleteUser(String userId) {
        UUID uId = UserHelper.parseUUID(userId);
        User userDoesNotExist = userRepo.findById(uId).orElseThrow(() -> new ResourceNotFoundException("User does not exist"));
        userRepo.delete(userDoesNotExist);
    }

    @Override
    public UserDto getUserById(String userID) {
        User user = userRepo.findById(UserHelper.parseUUID(userID)).orElseThrow(() -> new ResourceNotFoundException("User does not exist"));
        return modelMapper.map(user,UserDto.class);
    }

    @Override
    @Transactional
    public Iterable<UserDto> getAllUsers() {
        List<User> userList = userRepo.findAll();
        List<UserDto> userDtoList=new ArrayList<>();
        for(User u:userList){
            UserDto userDto = modelMapper.map(u, UserDto.class);
            userDtoList.add(userDto);
        }
        return userDtoList;
    }
}

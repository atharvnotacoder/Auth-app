package com.lwa.auth_app_backend.Services;

import com.lwa.auth_app_backend.Configs.AppConstants;
import com.lwa.auth_app_backend.Custom_Exceptions.ResourceNotFoundException;
import com.lwa.auth_app_backend.Dto.UserDto;
import com.lwa.auth_app_backend.Entities.Role;
import com.lwa.auth_app_backend.Entities.User;
import com.lwa.auth_app_backend.Repository.RoleRepo;
import com.lwa.auth_app_backend.Repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
//import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;

    private final EmailService emailService;
    @Override
    public UserDto registerUser(UserDto userDto) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        UserDto user = userService.createUser(userDto);

        return user;
    }

    @Override
    //method to generate and send reset otp
    public void sendResetOtp(String email){
        User user = userRepo.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User with this email doesn't exsits"));
        //Generate Otp
        String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        //calculate expiry time
        long expirytime = System.currentTimeMillis() + (15 * 60 * 1000);

        //update profile
        user.setResetOtp(otp);
        user.setResetOtpExpiresAt(expirytime);

        userRepo.save(user);

        try{
            //send the reset otp email]
            emailService.sendResetOtp(email,otp);
        }catch (Exception ex){
           ex.printStackTrace();
        }
    }

    @Override
    public void resetPassword(String email, String otp, String newPassword) {
        User user = userRepo.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User with this email not found"));
        if(user.getResetOtp()==null || !user.getResetOtp().equals(otp)){
            throw new RuntimeException("Invalid otp");
        }
        if(user.getResetOtpExpiresAt()<System.currentTimeMillis()){
            throw new RuntimeException("Otp expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetOtp(null);
        user.setResetOtpExpiresAt(0L);
        userRepo.save(user);
        emailService.sendPasswordUpdatedConfirmationEmail(user.getEmail(),user.getName());
    }

}

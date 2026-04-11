package com.lwa.auth_app_backend.Controllers;

import com.lwa.auth_app_backend.Dto.LoginRequest;
import com.lwa.auth_app_backend.Dto.TokenResponse;
import com.lwa.auth_app_backend.Dto.UserDto;
import com.lwa.auth_app_backend.Entities.User;
import com.lwa.auth_app_backend.MyAppSecurity.MyJwtService;
import com.lwa.auth_app_backend.Repository.UserRepo;
import com.lwa.auth_app_backend.Services.AuthService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final UserRepo userRepo;
    private final MyJwtService jwtService;
    private final ModelMapper modelMapper;
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest){
        //Authenticate
        Authentication authenticate = authenticate(loginRequest);
        User user=userRepo.findByEmail(loginRequest.email()).orElseThrow(()->new BadCredentialsException("Invalid UserName or Password"));
        if(!user.isEnable()){
            throw new DisabledException("User is disable");
        }

        //GENERAtE tOKEN
        String accessToken = jwtService.generateAccessToken(user);
        TokenResponse tokenResponse = TokenResponse.of(accessToken, "", jwtService.getAccessTtlSeconds(), modelMapper.map(user, UserDto.class));
        return ResponseEntity.ok(tokenResponse);

    }

    private Authentication authenticate(LoginRequest loginRequest) {
        try{
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(),loginRequest.password()));
        }catch (Exception ex){
            throw new BadCredentialsException("Username or password is invalid");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerUser(userDto));
    }
}

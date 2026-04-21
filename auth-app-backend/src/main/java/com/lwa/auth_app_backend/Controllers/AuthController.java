package com.lwa.auth_app_backend.Controllers;

import com.lwa.auth_app_backend.Custom_Exceptions.GlobalExceptionHandler;
import com.lwa.auth_app_backend.Dto.LoginRequest;
import com.lwa.auth_app_backend.Dto.TokenResponse;
import com.lwa.auth_app_backend.Dto.UserDto;
import com.lwa.auth_app_backend.Entities.RefreshToken;
import com.lwa.auth_app_backend.Entities.User;
import com.lwa.auth_app_backend.MyAppSecurity.MyJwtService;
import com.lwa.auth_app_backend.Repository.RefreshTokenRepo;
import com.lwa.auth_app_backend.Repository.UserRepo;
import com.lwa.auth_app_backend.Services.AuthService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final UserRepo userRepo;
    private final MyJwtService jwtService;
    private final ModelMapper modelMapper;
    private final RefreshTokenRepo refreshTokenRepo;
    public final Logger logger= LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest){

        //Authenticate
        Authentication authenticate = authenticate(loginRequest);
//        SecurityContextHolder.getContext().setAuthentication(authenticate);
        User user=userRepo.findByEmail(loginRequest.email()).orElseThrow(()->new BadCredentialsException("Invalid UserName or Password"));
//        User user = (User) authenticate.getPrincipal();
        if(!user.isEnable()){
            throw new DisabledException("User is disable");
        }
        String jti= UUID.randomUUID().toString();
        var refreshTokenOb= RefreshToken.builder()
                .jti(jti)
                .user(user)
                .createdAt(Instant.now())
                .expiredAt(Instant.now().plusSeconds(jwtService.getRefreshTtlSeconds()))
                .revoked(false)
                .build();

        refreshTokenRepo.save(refreshTokenOb);

//        String jti = UUID.randomUUID().toString();
//
//        RefreshToken refreshTokenOb = new RefreshToken();
//
//        refreshTokenOb.setJti(jti);
//        refreshTokenOb.setUser(user);
//        refreshTokenOb.setCreatedAt(Instant.now());
//        refreshTokenOb.setExpiredAt(
//                Instant.now().plusSeconds(jwtService.getRefreshTtlSeconds())
//        );
//        refreshTokenOb.setRevoked(false);

//        refreshTokenRepo.save(refreshTokenOb);
        //GENERAtE tOKEN
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken=jwtService.generateRefreshToken(user, refreshTokenOb.getJti());
        TokenResponse tokenResponse = TokenResponse.of(accessToken, refreshToken, jwtService.getAccessTtlSeconds(), modelMapper.map(user, UserDto.class));
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

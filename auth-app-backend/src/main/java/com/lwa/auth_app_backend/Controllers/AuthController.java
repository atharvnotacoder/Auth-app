package com.lwa.auth_app_backend.Controllers;

import com.lwa.auth_app_backend.Custom_Exceptions.GlobalExceptionHandler;
import com.lwa.auth_app_backend.Dto.LoginRequest;
import com.lwa.auth_app_backend.Dto.RefreshTokenRequest;
import com.lwa.auth_app_backend.Dto.TokenResponse;
import com.lwa.auth_app_backend.Dto.UserDto;
import com.lwa.auth_app_backend.Entities.RefreshToken;
import com.lwa.auth_app_backend.Entities.User;
import com.lwa.auth_app_backend.MyAppSecurity.CookieService;
import com.lwa.auth_app_backend.MyAppSecurity.MyJwtService;
import com.lwa.auth_app_backend.Repository.RefreshTokenRepo;
import com.lwa.auth_app_backend.Repository.UserRepo;
import com.lwa.auth_app_backend.Services.AuthService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
@CrossOrigin("http://localhost:5173")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final UserRepo userRepo;
    private final MyJwtService jwtService;
    private final ModelMapper modelMapper;
    private final RefreshTokenRepo refreshTokenRepo;
    private final CookieService cookieService;
    public final Logger logger= LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response){

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

        //GENERAtE tOKEN
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken=jwtService.generateRefreshToken(user, refreshTokenOb.getJti());

        //use-cookie service to attach refreshToken in cookies
        cookieService.attachRefreshCookie(response,refreshToken,(int)jwtService.getRefreshTtlSeconds());
        cookieService.addNoStoreHeaders(response);
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

    //Refresh token renew api
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(
            @RequestBody(required = false) RefreshTokenRequest body,
            HttpServletResponse response,
            HttpServletRequest request
            ){
            String refreshToken=readRefreshTokenFromRequest(body,request).orElseThrow(()->new BadCredentialsException("Refresh Token is missing"));

            if(!jwtService.isRefreshToken(refreshToken)){
                throw new BadCredentialsException("Invalid Refresh Token");
            }

        String jti = jwtService.getJti(refreshToken);
        UUID userId = jwtService.getUserId(refreshToken);
        RefreshToken storedRefreshToken = refreshTokenRepo.findByJti(jti).orElseThrow(()->new BadCredentialsException("Refresh Token is invalid"));
        if(storedRefreshToken.isRevoked()){
            throw new BadCredentialsException("Refresh Token is revoked");
        }
        if(storedRefreshToken.getExpiredAt().isBefore(Instant.now())){
            throw new BadCredentialsException("Refresh Token expried");
        }

        if(!storedRefreshToken.getUser().getId().equals(userId)){
            throw new BadCredentialsException("Refresh Token does not belong to this user");
        }

        //Refresh Token rotate
        storedRefreshToken.setRevoked(true);
        String newJti=UUID.randomUUID().toString();
        storedRefreshToken.setReplacedBy(newJti);
        refreshTokenRepo.save(storedRefreshToken);

        User user = storedRefreshToken.getUser();
        var newRefreshTokenOb=RefreshToken.builder()
                .jti(newJti)
                .user(user)
                .createdAt(Instant.now())
                .expiredAt(Instant.now().plusSeconds(jwtService.getRefreshTtlSeconds()))
                .revoked(false)
                .build();

        refreshTokenRepo.save(newRefreshTokenOb);
        String newaccessToken=jwtService.generateAccessToken(user);
        String newRefreshToken=jwtService.generateRefreshToken(user, newRefreshTokenOb.getJti());
        cookieService.attachRefreshCookie(response,newRefreshToken,(int)jwtService.getRefreshTtlSeconds());
        cookieService.addNoStoreHeaders(response);
        return  ResponseEntity.ok(TokenResponse.of(newaccessToken,newRefreshToken,jwtService.getAccessTtlSeconds(),modelMapper.map(user,UserDto.class)));

    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request,HttpServletResponse response){
        readRefreshTokenFromRequest(null,request).ifPresent(token->{
            try{
                if(jwtService.isRefreshToken(token)){
                    String jti=jwtService.getJti(token);
                    refreshTokenRepo.findByJti(jti).ifPresent(rt->{
                        rt.setRevoked(true);
                        refreshTokenRepo.save(rt);
                    });
                }
                }catch(JwtException ignored){
            }
        });

        cookieService.clearRefreshCookie(response);
        cookieService.addNoStoreHeaders(response);
        SecurityContextHolder.clearContext();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }



    private Optional<String> readRefreshTokenFromRequest(RefreshTokenRequest body, HttpServletRequest request) {
        //1.Prefer reading refresh token from cookie
        if (request.getCookies() != null) {
            Optional<String> fromCookie = Arrays.stream(
                            request.getCookies()
                    ).filter(c -> cookieService.getRefreshTokenCookieName().equals(c.getName()))
                    .map(Cookie::getValue)
                    .filter(v -> !v.isBlank())
                    .findFirst();

            if (fromCookie.isPresent()) {
                return fromCookie;
            }

        }

        //2.If cookie not present, read from body
        if (body != null && body.refreshToken() != null && !body.refreshToken().isBlank()) {
            return Optional.of(body.refreshToken());
        }

        return Optional.empty();
    }
    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerUser(userDto));
    }
}

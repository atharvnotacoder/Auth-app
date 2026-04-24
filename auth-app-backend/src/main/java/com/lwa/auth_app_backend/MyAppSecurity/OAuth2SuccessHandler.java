package com.lwa.auth_app_backend.MyAppSecurity;

import com.lwa.auth_app_backend.Entities.Provider;
import com.lwa.auth_app_backend.Entities.RefreshToken;
import com.lwa.auth_app_backend.Entities.User;
import com.lwa.auth_app_backend.Repository.RefreshTokenRepo;
import com.lwa.auth_app_backend.Repository.UserRepo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final MyJwtService jwtService;
    private final CookieService cookieService;
    private final RefreshTokenRepo refreshTokenRepo;
    private final UserRepo userRepo;
    private final Logger logger= LoggerFactory.getLogger(this.getClass());
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        logger.info("Successful Authentication");
        logger.info(authentication.toString());

        OAuth2User oAuth2User=(OAuth2User)authentication.getPrincipal();
        //Identify user
        String registrationId="unknown";
        if(authentication instanceof OAuth2AuthenticationToken token){
            registrationId=token.getAuthorizedClientRegistrationId();
        }
        logger.info("registration id"+registrationId);
        logger.info("user"+oAuth2User.getAttributes().toString());

        User user;
        switch (registrationId){
            case "google"-> {
                String googleId = oAuth2User.getAttributes().getOrDefault("sub", "").toString();
                String email = oAuth2User.getAttributes().getOrDefault("email", "").toString();
                String name = oAuth2User.getAttributes().getOrDefault("name", "").toString();
                String picture = oAuth2User.getAttributes().getOrDefault("picture", "").toString();
                user = User.builder()
                        .email(email)
                        .name(name)
                        .image(picture)
                        .enable(true)
                        .provider(Provider.GOOGLE)
                        .build();

                userRepo.findByEmail(email).ifPresentOrElse(user1 -> {
                    logger.info("User already exist");
                    logger.info(user1.toString());
                }, () -> {
                    userRepo.save(user);
                });
            }
            default -> throw
                    new RuntimeException("Invalid registration id");
        }
        //refresh token
        String jti = UUID.randomUUID().toString();
        RefreshToken refreshtokenOb = RefreshToken.builder()
                .jti(jti)
                .user(user)
                .revoked(false)
                .createdAt(Instant.now())
                .expiredAt(Instant.now().plusSeconds(jwtService.getRefreshTtlSeconds()))
                .build();

        refreshTokenRepo.save(refreshtokenOb);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken =jwtService.generateRefreshToken(user,refreshtokenOb.getJti());

       cookieService.attachRefreshCookie(response,refreshToken,(int) jwtService.getRefreshTtlSeconds());

        response.getWriter().write("Login Success");
    }
}

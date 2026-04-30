package com.lwa.auth_app_backend.Configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lwa.auth_app_backend.Dto.ApiError;
import com.lwa.auth_app_backend.MyAppSecurity.CustomUserDetailService;
import com.lwa.auth_app_backend.MyAppSecurity.JwtAuthFilter;
import com.lwa.auth_app_backend.MyAppSecurity.MyJwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor

public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
     @Bean
    public PasswordEncoder passwordEncoder(){
     return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

         http.csrf(AbstractHttpConfigurer::disable).
         cors(Customizer.withDefaults()).sessionManagement(sm->sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).
         authorizeHttpRequests(authorizeHttpRequest->
                 authorizeHttpRequest.requestMatchers("/api/v1/auth/register").permitAll()
                 .requestMatchers("/api/v1/auth/login").permitAll()
                         .requestMatchers("/api/v1/auth/refresh").permitAll()
                         .requestMatchers("/api/v1/auth/logout").permitAll()
                         .requestMatchers("/error").permitAll()
                         .anyRequest().authenticated())
                 .oauth2Login(oauth2->
                         oauth2.successHandler(authenticationSuccessHandler)
                                 .failureHandler(null))
                 .logout(AbstractHttpConfigurer::disable)
                 .exceptionHandling(ex->ex.authenticationEntryPoint((request, response, e) ->{
                     e.printStackTrace();
                     response.setStatus(401);
                     response.setContentType("applicaion/json");
                     String message=e.getMessage();
                     String error=(String) e.getMessage();
                     if(error!=null){
                            message=error;
                     }

//                     Map<String,String> errorMap=Map.of("message",message,"statusCOde",Integer.toString(401));


//                     .message(message)
//                     .status(401)
//                     .build();
                     var apiError= ApiError.of(HttpStatus.BAD_REQUEST.value(),"Unauthorized Access",message,request.getRequestURI());
                     var objectMapper=new ObjectMapper();
                     response.getWriter().write(objectMapper.writeValueAsString(apiError));
                         }))
                 .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

         return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration, CustomUserDetailService customUserDetailService, PasswordEncoder passwordEncoder) throws Exception {
         DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
         authProvider.setUserDetailsService(customUserDetailService);
         authProvider.setPasswordEncoder(passwordEncoder);
         return new ProviderManager(authProvider);
    }

//    @Bean
//    public UserDetailsService users(){
//        User.UserBuilder userBuilder=User.withDefaultPasswordEncoder();
//        UserDetails user1 = userBuilder.username("Renu").password("abc").roles("ADMIN").build();
//        UserDetails user2 = userBuilder.username("Atharv").password("123").roles("USER").build();
//    return new InMemoryUserDetailsManager(user1,user2);
//    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
         var config=new CorsConfiguration();
         config.setAllowedOrigins(List.of("http://localhost:5173"));
         config.setAllowedMethods(List.of("GET","POST","PUT","DELETE"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        var source=new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",config);
        return source;
     }
}

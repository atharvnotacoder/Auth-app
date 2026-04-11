package com.lwa.auth_app_backend.MyAppSecurity;

import com.lwa.auth_app_backend.Entities.Role;
import com.lwa.auth_app_backend.Entities.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Getter
@Setter
public class MyJwtService {

    private final SecretKey key;
    private final long accessTtlSeconds;
    private final long refreshTtlSeconds;
    private final String issuer;

    public MyJwtService(@Value("${security.jwt.secret}") String secret,@Value("${security.jwt.access-ttl-seconds}") long accessTtlSeconds,@Value("${security.jwt.refresh-ttl-seconds}") long refreshTtlSeconds,@Value("${security.jwt.issuer}") String issuer){

        if(secret==null || secret.length()<64){
            throw new IllegalArgumentException("Invalid Secret");
        }
        this.key= Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTtlSeconds=accessTtlSeconds;
        this.refreshTtlSeconds=refreshTtlSeconds;
        this.issuer=issuer;
    }

    //GENERATE TOKEN
    public String generateAccessToken(User user){
        Instant now=Instant.now();
        List<String> roles=user.getRoles()==null?List.of():
        user.getRoles().stream().map(Role::getRoleName).toList();
        return Jwts.builder().id(UUID.randomUUID().toString())
                .subject(user.getId().toString())
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(accessTtlSeconds)))
                .claims(Map.of(
                        "email",user.getEmail(),
                        "roles",roles,
                        "typ","access"
                )).signWith(key, SignatureAlgorithm.HS512).compact();
    }

    //GENERATE REFRESH TOKEN
    public String generateRefreshToken(User user,String jti){
        Instant now=Instant.now();
        return Jwts.builder().id(jti)
                .subject(user.getId().toString())
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(accessTtlSeconds)))
                .claims(Map.of("typ","refresh"
                )).signWith(key, SignatureAlgorithm.HS512).compact();
    }

    //PARSE TOKEN
    public Jws<Claims> parse(String token){
        try {
            {
                return Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            }
        }
    catch (JwtException ex){
            throw ex;
    }
    }

    //TOKEN ACCESS VALIDATION
    public boolean isAccessToken(String token){
        Claims claim=parse(token).getPayload();
        return "access".equals(claim.get("typ"));
    }

    //TOKEN REFRESH VALIDATION
    public boolean isRefreshToken(String token){
        Claims claim=parse(token).getPayload();
        return "refresh".equals(claim.get("typ"));
    }

    //FETCH USER ID
    public UUID getUserId(String token){
        Claims claim=parse(token).getPayload();
        return UUID.fromString(claim.getSubject());
    }

    //FETCH TOKEN ID
    public String getJti(String token){
        return  parse(token).getPayload().getId();
    }

    public List<String> getRoles(String token){
        Claims c=parse((token)).getPayload();
        return (List<String>) c.get("roles");
    }

    public String getEmail(String token){
        Claims c = parse(token).getPayload();
        return (String) c.get("email");
    }


}

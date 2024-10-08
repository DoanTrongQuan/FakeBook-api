package org.example.identityservice.utils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.identityservice.entity.User;
import org.example.identityservice.entity.UserRole;
import org.example.identityservice.exceptions.InvalidParamException;
import org.example.identityservice.repository.UserRoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.SecureRandom;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenUtils {
    @Value("${jwt.expiration}")
    private int expiration; //save to an environment variable
    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${jwt.expirationRefreshToken}")
    private int expirationRefreshToken;

    @Autowired
    private UserRoleRepo userRoleRepo;
    public String generateToken(User user) throws Exception{
        //properties => claims
        Map<String, Object> claims = new HashMap<>();
        //this.generateSecretKey();
        Set<UserRole> userRoles = userRoleRepo.findAllByUser(user);


        claims.put("scope", userRoles.stream().map(role ->  role.getRole().getRoleName()).collect(Collectors.joining(" ")));
        claims.put("email", user.getEmail());

        try {
            String token = Jwts.builder()
                    .setClaims(claims) //how to extract claims from this ?
                    .setSubject(user.getEmail())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
            return token;
        }catch (Exception e) {
            //you can "inject" Logger, instead System.out.println
            throw new InvalidParamException("Cannot create jwt token, error: "+e.getMessage());
            //return null;
        }
    }

    public String generateRefreshToken(User user) throws Exception{
        Map<String, Object> claims = new HashMap<>();
        claims.put("Email", user.getEmail());
        try {
          String refreshToken = Jwts.builder()
                  .setClaims(claims)
                  .setSubject(user.getEmail())
                  .setExpiration(new Date(System.currentTimeMillis() + expirationRefreshToken * 1000L))
                  .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                  .compact();
            return refreshToken;
        }catch (Exception e) {
            throw new InvalidParamException("Cannot create refresh token, error: "+e.getMessage());
        }
    }

    private Key getSignInKey() {
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        //Keys.hmacShaKeyFor(Decoders.BASE64.decode("TaqlmGv1iEDMRiFp/pHuID1+T84IABfuA0xXh4GhiUI="));
        return Keys.hmacShaKeyFor(bytes);
    }
    private String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[32]; // 256-bit key
        random.nextBytes(keyBytes);
        String secretKey = Encoders.BASE64.encode(keyBytes);
        return secretKey;
    }
    private Claims extractAllClaims (String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public  <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    //check expiration
    public boolean isTokenExpired(String token) {
        Date expirationDate = this.extractClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    public boolean validateToken(String token, User user) {
        String email = extractEmail(token);
        log.info("UserName :" + user.getUsername());
        return (email.equals(user.getUsername()))
                && !isTokenExpired(token);
    }
}

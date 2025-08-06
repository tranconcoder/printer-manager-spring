package com.tvconss.printermanagerspring.service.impl;

import com.tvconss.printermanagerspring.dto.internal.jwt.JwtPayload;
import com.tvconss.printermanagerspring.service.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.time.Instant;
import java.util.*;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.access_token_expire_time}")
    private int accessTokenExpireTime;

    @Value("${jwt.refresh_token_expire_time}")
    private int refreshTokenExpireTime;

    @Override
    public String generateAccessToken(Long jti, PrivateKey privateKey, JwtPayload user) {
        Instant now = Instant.now();
        Instant expireAt = now.plusSeconds(this.accessTokenExpireTime);

        System.out.println(now);
        System.out.println(expireAt);

        return Jwts.builder()
                .setId(jti.toString())
                .setSubject(user.getUserId().toString())
                .claim("userId", user.getUserId())
                .claim("userFirstName", user.getUserFirstName())
                .claim("userLastName", user.getUserLastName())
                .claim("userEmail", user.getUserEmail())
                .claim("userGender", user.isUserGender())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expireAt))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    @Override
    public String generateRefreshToken(Long jti, PrivateKey privateKey, JwtPayload user) {
        Instant now = Instant.now();
        Instant expireAt = now.plusSeconds(this.refreshTokenExpireTime);

        return Jwts.builder()
                .setId(jti.toString())
                .setSubject(user.getUserId().toString())
                .claim("userId", user.getUserId())
                .claim("userFirstName", user.getUserFirstName())
                .claim("userLastName", user.getUserLastName())
                .claim("userEmail", user.getUserEmail())
                .claim("userGender", user.isUserGender())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expireAt))
                .signWith(privateKey, SignatureAlgorithm.RS512)
                .compact();
    }

    @Override
    public Map<String, String> generateJwtTokenPair(Long jti, PrivateKey privateKey, JwtPayload user) {

        Map<String, String> jwtTokenPair =  new HashMap<>();

        jwtTokenPair.put("accessToken", generateAccessToken(jti, privateKey, user));
        jwtTokenPair.put("refreshToken", generateRefreshToken(jti, privateKey, user));

        return jwtTokenPair;
    }
}

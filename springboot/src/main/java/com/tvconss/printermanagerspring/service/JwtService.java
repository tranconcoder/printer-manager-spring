package com.tvconss.printermanagerspring.service;

import com.tvconss.printermanagerspring.dto.internal.jwt.JwtPayload;

import java.security.PrivateKey;
import java.util.Map;
import java.util.UUID;

public interface JwtService {

    public String generateAccessToken(Long jti, PrivateKey privateKey, JwtPayload user);

    public String generateRefreshToken(Long jti, PrivateKey privateKey, JwtPayload user);

    public Map<String, String> generateJwtTokenPair(Long jti, PrivateKey privateKey, JwtPayload user);
}

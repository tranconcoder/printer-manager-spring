package com.tvconss.printermanagerspring.service;

import com.tvconss.printermanagerspring.dto.internal.jwt.JwtPayload;

import java.security.PrivateKey;
import java.util.Map;
import java.util.UUID;

public interface JwtService {

    public String generateAccessToken(PrivateKey privateKey, JwtPayload user);

    public String generateRefreshToken(UUID uuid, PrivateKey privateKey, JwtPayload user);

    public Map<String, String> generateJwtTokenPair(UUID uuid, PrivateKey privateKey, JwtPayload user);
}

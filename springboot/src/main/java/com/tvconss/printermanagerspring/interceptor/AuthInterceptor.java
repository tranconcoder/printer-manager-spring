package com.tvconss.printermanagerspring.interceptor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tvconss.printermanagerspring.entity.KeyTokenEntity;
import com.tvconss.printermanagerspring.enums.ErrorCode;
import com.tvconss.printermanagerspring.exception.ErrorResponse;
import com.tvconss.printermanagerspring.repository.KeyTokenRedisRepository;
import com.tvconss.printermanagerspring.service.JwtService;
import com.tvconss.printermanagerspring.util.JwtUtil;
import com.tvconss.printermanagerspring.util.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.security.PublicKey;
import java.util.Base64;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;
    private final KeyTokenRedisRepository keyTokenRedisRepository;
    private final RedisUtil redisUtil;
    private final JwtService jwtService;

    public AuthInterceptor(ObjectMapper objectMapper,
                           KeyTokenRedisRepository keyTokenRedisRepository,
                           RedisUtil redisUtil,
                           JwtService jwtService) {
        this.objectMapper = objectMapper;
        this.keyTokenRedisRepository = keyTokenRedisRepository;
        this.redisUtil = redisUtil;
        this.jwtService = jwtService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null) {
            throw new ErrorResponse(ErrorCode.AUTH_MISSING_TOKEN, "Please enter access token");
        }

//        Validate token in header
        if (!authorizationHeader.startsWith("Bearer ")) {
            throw new ErrorResponse(ErrorCode.AUTH_INVALID_TOKEN, "Please use bearer token");
        }

//        Get token and verify
        String token = authorizationHeader.substring(7);
        if (token.isEmpty()) {
            throw new ErrorResponse(ErrorCode.AUTH_MISSING_TOKEN, "Token is empty");
        }

//        Parse payload -> get user id -> get key token
        Base64.Decoder decoder = Base64.getDecoder();

        String[] segments = token.split("\\.");
        if (segments.length != 3) {
            throw new ErrorResponse(ErrorCode.AUTH_INVALID_TOKEN, "Invalid token");
        }
        String payload = new String(decoder.decode(segments[1]));

        JsonNode jsonNode = this.objectMapper.readTree(payload);
        Long userId = jsonNode.get("userId").asLong(-1);
        Long jti = jsonNode.get("jti").asLong(-1);

        if (userId == -1 || jti == -1) {
            throw new ErrorResponse(ErrorCode.AUTH_INVALID_TOKEN, "Invalid token");
        }

//        Get key token with userId
        String keyTokenKey = this.redisUtil.getKeyTokenKey(userId, jti);
        KeyTokenEntity keyToken = this.keyTokenRedisRepository.findById(keyTokenKey).orElse(null);

        if (keyToken == null) {
            throw new ErrorResponse(ErrorCode.AUTH_INVALID_TOKEN, "Invalid token");
        }

//        Verify jwt
        try {
            PublicKey publicKey = keyToken.getPublicKeyAsObject();
            Claims jwtClaims = this.jwtService.verifyToken(token, publicKey, userId, jti);

//            Set request attribute
            request.setAttribute("jwtClaims", jwtClaims);
        } catch(Exception e) {
            throw new ErrorResponse(ErrorCode.AUTH_FAILED, e.getMessage());
        }

        return true;
    }
}

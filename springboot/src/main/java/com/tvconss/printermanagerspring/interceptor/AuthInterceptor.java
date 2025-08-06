package com.tvconss.printermanagerspring.interceptor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tvconss.printermanagerspring.dto.internal.KeyToken;
import com.tvconss.printermanagerspring.dto.internal.jwt.JwtPayload;
import com.tvconss.printermanagerspring.enums.ErrorCode;
import com.tvconss.printermanagerspring.exception.ErrorResponse;
import com.tvconss.printermanagerspring.service.impl.KeyTokenServiceImpl;
import com.tvconss.printermanagerspring.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.security.PublicKey;
import java.util.Base64;
import java.util.UUID;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;
    private final KeyTokenServiceImpl keyTokenService;

    public AuthInterceptor(ObjectMapper objectMapper, KeyTokenServiceImpl keyTokenService) {
        this.objectMapper = objectMapper;
        this.keyTokenService = keyTokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null) {
            throw new ErrorResponse(ErrorCode.AUTH_FAILED, "Please enter access token");
        }

//        Validate token in header
        if (!authorizationHeader.startsWith("Bearer ")) {
            throw new ErrorResponse(ErrorCode.AUTH_FAILED, "Please use bearer token");
        }

//        Get token and verify
        String token = authorizationHeader.substring(7);
        if (token.length() == 0) {
            throw new ErrorResponse(ErrorCode.AUTH_FAILED, "Token is empty");
        }

//        Parse payload -> get user id -> get key token
        Base64.Decoder decoder = Base64.getDecoder();

        String[] segments = token.split("\\.");
        if (segments.length != 3) {
            throw new ErrorResponse(ErrorCode.AUTH_FAILED, "Invalid token");
        }
        String payload = new String(decoder.decode(segments[1]));

        JsonNode jsonNode = this.objectMapper.readTree(payload);
        Long userId = jsonNode.get("userId").asLong(-1);
        Long jti = jsonNode.get("jti").asLong(-1);

        if (userId == -1 || jti == -1) {
            throw new ErrorResponse(ErrorCode.AUTH_FAILED, "Invalid token");
        }

//        Get key token with userId
        KeyToken keyToken = this.keyTokenService.getKeyTokenByUserId(userId, jti);
        if (keyToken == null) {
            throw new ErrorResponse(ErrorCode.AUTH_FAILED, "Invalid token");
        }

//        Verify jwt
        try {
            PublicKey publicKey = JwtUtil.convertBase64ToPublicKey(keyToken.getPublicKey());

            Claims jwtPayload = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
//            Set request attribute
            request.setAttribute("jwtPayload", jwtPayload);
        } catch(Exception e) {
            throw new ErrorResponse(ErrorCode.AUTH_FAILED, e.getMessage());
        }

        return true;
    }
}

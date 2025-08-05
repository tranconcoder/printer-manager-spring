package com.tvconss.printermanagerspring.util;

import com.tvconss.printermanagerspring.dto.internal.jwt.JwtPayload;
import com.tvconss.printermanagerspring.dto.response.user.AuthResponse;
import com.tvconss.printermanagerspring.dto.response.user.JwtTokenPair;
import com.tvconss.printermanagerspring.dto.response.user.User;
import com.tvconss.printermanagerspring.entity.UserEntity;
import com.tvconss.printermanagerspring.enums.ErrorCode;
import com.tvconss.printermanagerspring.exception.ErrorResponse;
import com.tvconss.printermanagerspring.service.KeyTokenService;
import com.tvconss.printermanagerspring.service.impl.JwtServiceImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.*;
import java.util.Map;
import java.util.UUID;

@Component
public class AuthUtil {

    private JwtServiceImpl jwtService;
    private KeyTokenService keyTokenService;
    private PasswordEncoder passwordEncoder;
    private KeyPairGenerator keyPairGenerator;

    public AuthUtil(JwtServiceImpl jwtService, KeyTokenService keyTokenService, PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.keyTokenService = keyTokenService;
        this.passwordEncoder = passwordEncoder;

        try {
            this.keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            this.keyPairGenerator.initialize(2048);
        } catch(NoSuchAlgorithmException e) {
            this.keyPairGenerator = null;
        }
    }

    public AuthResponse generateAuthInformation(UserEntity user) {

//        Create access token and refresh token (key token)
//        Step 1: generate private key and public key to create JWT
        if (this.keyPairGenerator == null) {
            throw new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR, "Can not generate key!");
        }

        KeyPair rsaKeyPair = this.keyPairGenerator.generateKeyPair();
        PublicKey publicKey = rsaKeyPair.getPublic();
        PrivateKey privateKey = rsaKeyPair.getPrivate();
        JwtPayload jwtPayload = new JwtPayload();

        jwtPayload.setUserId(user.getUserId());
        jwtPayload.setUserEmail(user.getUserEmail());
        jwtPayload.setUserFirstName(user.getUserFirstName());
        jwtPayload.setUserLastName(user.getUserLastName());
        jwtPayload.setUserGender(user.isUserGender());

//        Step 2: use private key to create JWT
        UUID jitUUID = UUID.randomUUID();
        Map<String, String> jwtTokenPair = new JwtServiceImpl().generateJwtTokenPair(
                jitUUID,
                privateKey,
                jwtPayload);


//        Create key token
        this.keyTokenService.createKeyToken(publicKey, user.getUserId(), jitUUID);


//        Response token and information to user registration
        AuthResponse response = new AuthResponse();

//        User information
        User userResponse = new User();
        userResponse.loadFromEntity(user);

//        Jwt token pair information
        JwtTokenPair jwtTokenPairResponse = new JwtTokenPair();
        jwtTokenPairResponse.setAccessToken(jwtTokenPair.get("accessToken"));
        jwtTokenPairResponse.setRefreshToken(jwtTokenPair.get("refreshToken"));

        response.setUser(userResponse);
        response.setToken(jwtTokenPairResponse);

        return response;
    }
}

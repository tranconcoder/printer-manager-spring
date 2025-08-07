package com.tvconss.printermanagerspring.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tvconss.printermanagerspring.dto.request.auth.AuthLoginRequest;
import com.tvconss.printermanagerspring.dto.request.auth.AuthRegisterRequest;
import com.tvconss.printermanagerspring.dto.response.auth.AuthResponse;
import com.tvconss.printermanagerspring.dto.response.auth.JwtTokenPair;
import com.tvconss.printermanagerspring.entity.KeyTokenEntity;
import com.tvconss.printermanagerspring.entity.UserEntity;
import com.tvconss.printermanagerspring.enums.ErrorCode;
import com.tvconss.printermanagerspring.exception.ErrorResponse;
import com.tvconss.printermanagerspring.repository.KeyTokenRedisRepository;
import com.tvconss.printermanagerspring.repository.UserRepository;
import com.tvconss.printermanagerspring.service.AuthService;
import com.tvconss.printermanagerspring.service.JwtService;
import com.tvconss.printermanagerspring.util.AuthUtil;
import com.tvconss.printermanagerspring.util.RedisUtil;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.*;
import java.util.Base64;

@Service
public class AuthServiceImpl implements AuthService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private ObjectMapper objectMapper;
    private KeyTokenRedisRepository keyTokenRedisRepository;
    private RedisUtil redisUtil;
    private AuthUtil authUtil;
    private JwtService jwtService;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           AuthUtil authUtil,
                           ObjectMapper objectMapper,
                           KeyTokenRedisRepository keyTokenRedisRepository,
                           RedisUtil redisUtil,
                           JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authUtil = authUtil;
        this.objectMapper = objectMapper;
        this.keyTokenRedisRepository = keyTokenRedisRepository;
        this.redisUtil = redisUtil;
        this.jwtService = jwtService;
    }


    public AuthResponse register(AuthRegisterRequest data) {
//        Check email is not existed
        String email = data.getEmail();
        UserEntity userExisted = this.userRepository.findByUserEmail(email).orElse(null);

        if (userExisted != null) {
            throw new ErrorResponse(ErrorCode.AUTH_USER_ALREADY_EXIST);
        }

//      Hash password
        String hashPassword = passwordEncoder.encode(data.getPassword());

//        Create new entity
        UserEntity userEntity = new UserEntity();
        boolean isMale = data.getGender().equals("male");

        userEntity.setPassword(hashPassword);
        userEntity.setUserEmail(email);
        userEntity.setUserFirstName(data.getFirstName());
        userEntity.setUserLastName(data.getLastName());
        userEntity.setUserGender(isMale);

        UserEntity savedUser = this.userRepository.save(userEntity);

        if (savedUser.getUserId() == null) {
            throw new ErrorResponse(ErrorCode.AUTH_ERROR_INTERNAL);
        }

        return this.authUtil.generateAuthInformation(savedUser, null);
    }

    public AuthResponse login(AuthLoginRequest data) {
        String email = data.getEmail();
        String password = data.getPassword();

//        Check user is exist
        UserEntity user = this.userRepository.findByUserEmail(email).orElse(null);

        if (user == null) {
            throw new ErrorResponse(ErrorCode.AUTH_FAILED);
        }

//        Check password is matches?
        boolean isPasswordMatches = this.passwordEncoder.matches(password, user.getPassword());
        if (!isPasswordMatches) {
            throw new ErrorResponse(ErrorCode.AUTH_FAILED);
        }

        return authUtil.generateAuthInformation(user, null);
    }

    public JwtTokenPair refreshToken(String refreshToken) {

//        Get payload from refresh token
        String[] segments = refreshToken.split("\\.");
        String payload = segments.length == 3 ? segments[1] : null;

        if (payload == null) {
            throw new ErrorResponse(ErrorCode.AUTH_INVALID_TOKEN, "Invalid refresh token format");
        }

//            Decode payload from base64 to String
        Base64.Decoder decoder = Base64.getDecoder();
        String decodedPayload = new String(decoder.decode(payload));

//            Decode payload from String to JwtPayload
        long userId, jti;
        String userEmail, userFirstName, userLastName;
        boolean userGender;

        try {
            JsonNode jsonNode = this.objectMapper.readTree(decodedPayload);

            userId = jsonNode.get("userId").asLong(-1);
            jti = jsonNode.get("jti").asLong(-1);
            userEmail = jsonNode.get("userEmail").asText();
            userFirstName = jsonNode.get("userFirstName").asText();
            userLastName = jsonNode.get("userLastName").asText();
            userGender = jsonNode.get("userGender").asBoolean();

            if (userId == -1 || jti == -1 || userEmail.isEmpty() || userFirstName.isEmpty() || userLastName.isEmpty()) {
                throw new Exception();
            }
        } catch(Exception e) {
            throw new ErrorResponse(ErrorCode.AUTH_INVALID_TOKEN, "Invalid refresh token payload");
        }

//            Get key token
        String keyTokenKey = this.redisUtil.getKeyTokenKey(userId, jti);
        KeyTokenEntity keyToken = this.keyTokenRedisRepository.findById(keyTokenKey)
                .orElse(null);

        if (keyToken == null) {
            throw new ErrorResponse(ErrorCode.AUTH_INVALID_TOKEN, "Invalid refresh token");
        }

        try {
//             Verify refresh token with public key
            PublicKey publicKey = keyToken.getPublicKeyAsObject();

            this.jwtService.verifyToken(refreshToken, publicKey, userId, jti);
        } catch (IllegalArgumentException e) {
            throw new ErrorResponse(ErrorCode.AUTH_INVALID_TOKEN, "Refresh token is invalid");
        } catch (Exception ex) {
            throw new ErrorResponse(ErrorCode.AUTH_INVALID_TOKEN, ex.getMessage());
        }

//            Create new token pair
        UserEntity user = new UserEntity();
        user.setUserId(userId);
        user.setUserEmail(userEmail);
        user.setUserFirstName(userFirstName);
        user.setUserLastName(userLastName);
        user.setUserGender(userGender);

        AuthResponse authResponse = this.authUtil.generateAuthInformation(user, jti);

        return authResponse.getToken();
    }
}

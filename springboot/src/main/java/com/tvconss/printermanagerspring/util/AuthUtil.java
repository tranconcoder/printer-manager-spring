package com.tvconss.printermanagerspring.util;

import com.tvconss.printermanagerspring.dto.internal.jwt.JwtPayload;
import com.tvconss.printermanagerspring.dto.response.auth.AuthResponse;
import com.tvconss.printermanagerspring.dto.response.auth.JwtTokenPair;
import com.tvconss.printermanagerspring.dto.response.user.UserResponse;
import com.tvconss.printermanagerspring.entity.KeyTokenEntity;
import com.tvconss.printermanagerspring.entity.UserEntity;
import com.tvconss.printermanagerspring.enums.ErrorCode;
import com.tvconss.printermanagerspring.exception.ErrorResponse;
import com.tvconss.printermanagerspring.repository.KeyTokenRedisRepository;
import com.tvconss.printermanagerspring.service.impl.JwtServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.*;
import java.util.Base64;
import java.util.Map;

@Component
public class AuthUtil {

    private final JwtServiceImpl jwtService;
    private final KeyPairGenerator keyPairGenerator;
    private final RedisUtil redisUtil;
    private final KeyTokenRedisRepository keyTokenRedisRepository;

    @Value("${jwt.refresh_token_expire_time}")
    private long refreshTokenExpireTime;

    public AuthUtil(JwtServiceImpl jwtService,
                    RedisUtil redisUtil,
                    KeyTokenRedisRepository keyTokenRedisRepository,
                    @Value("${jwt.rsa_key_size}") int rsaKeySize) {
        this.jwtService = jwtService;
        this.redisUtil = redisUtil;
        this.keyTokenRedisRepository = keyTokenRedisRepository;

        System.out.println("RSA Key Size: " + rsaKeySize);

        KeyPairGenerator keyPairGeneratorTemp;
        try {
            keyPairGeneratorTemp = KeyPairGenerator.getInstance("RSA");
            keyPairGeneratorTemp .initialize(rsaKeySize);
        } catch(NoSuchAlgorithmException e) {
            keyPairGeneratorTemp = null;
        }

        this.keyPairGenerator = keyPairGeneratorTemp;
    }

    public AuthResponse generateAuthInformation(UserEntity user, Long newJti) {
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
        Long jti = newJti != null ? newJti : this.redisUtil.getNextJti(user.getUserId());
        Map<String, String> jwtTokenPair = this.jwtService.generateJwtTokenPair(
                jti,
                privateKey,
                jwtPayload);


//        Create key token
        KeyTokenEntity keyToken = new KeyTokenEntity(
                this.redisUtil.getKeyTokenKey(user.getUserId(), jti),
                user.getUserId(),
                jti,
                Base64.getEncoder().encodeToString(publicKey.getEncoded()),
                refreshTokenExpireTime
        );

        this.keyTokenRedisRepository.createOrUpdateKeyToken(keyToken);

//        User information
        UserResponse userResponse = new UserResponse();
        userResponse.loadFromEntity(user);

//        Jwt token pair information
        JwtTokenPair jwtTokenPairResponse = new JwtTokenPair(
                jwtTokenPair.get("accessToken"),
                jwtTokenPair.get("refreshToken")
        );

//        Response token and information to user registration
        return new AuthResponse(userResponse, jwtTokenPairResponse);
    }
}

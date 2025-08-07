package com.tvconss.printermanagerspring.service.impl;

import com.tvconss.printermanagerspring.entity.KeyTokenEntity;
import com.tvconss.printermanagerspring.enums.ErrorCode;
import com.tvconss.printermanagerspring.exception.ErrorResponse;
import com.tvconss.printermanagerspring.repository.KeyTokenRedisRepository;
import com.tvconss.printermanagerspring.service.KeyTokenService;
import com.tvconss.printermanagerspring.util.RedisUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.util.Base64;

@Service
public class KeyTokenServiceImpl implements KeyTokenService {

    private final KeyTokenRedisRepository keyTokenRedisRepository;
    private final RedisUtil redisUtil ;

    @Value("${jwt.refresh_token_expire_time}")
    private long refreshTokenExpireTime;

    public KeyTokenServiceImpl(RedisUtil redisUtil, KeyTokenRedisRepository keyTokenRedisRepository) {
        this.redisUtil = redisUtil;
        this.keyTokenRedisRepository = keyTokenRedisRepository;
    }
}

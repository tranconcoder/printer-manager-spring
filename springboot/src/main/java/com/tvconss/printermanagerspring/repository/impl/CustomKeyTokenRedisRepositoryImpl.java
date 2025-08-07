package com.tvconss.printermanagerspring.repository.impl;

import com.tvconss.printermanagerspring.entity.KeyTokenEntity;
import com.tvconss.printermanagerspring.repository.CustomKeyTokenRedisRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Map;

@Repository
public class CustomKeyTokenRedisRepositoryImpl implements CustomKeyTokenRedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final Long refreshTokenExpireTime;

    public CustomKeyTokenRedisRepositoryImpl(RedisTemplate<String, Object> redisTemplate,
                                             @Value("${jwt.refresh_token_expire_time}") Long refreshTokenExpireTime) {
        this.redisTemplate = redisTemplate;
        this.refreshTokenExpireTime = refreshTokenExpireTime;
    }

    @Override
    public void createOrUpdateKeyToken(KeyTokenEntity keyToken) {
        // Sử dụng SessionCallback để đảm bao atomic operation
        redisTemplate.execute(new SessionCallback<Object>() {
            @Override
            @SuppressWarnings( "unchecked")
            public Object execute(@NotNull RedisOperations operations) throws DataAccessException {
                String key = "user:key_token:" + keyToken.getUserId() + ":" + keyToken.getJti();
                String publicKey = keyToken.getPublicKey();

                System.out.println("PUBLIC KEY IN REDIS: " + publicKey);

                Map<String, Object> keyTokenMap = Map.of(
                    "userId", keyToken.getUserId(),
                    "jti", keyToken.getJti(),
                    "publicKey", publicKey
                );

                HashOperations<String, String, Object> hashOperations = operations.opsForHash();

                hashOperations.putAll(key, keyTokenMap);
                operations.expire(key, Duration.ofSeconds(refreshTokenExpireTime));

                return null;
            }
        });

    }
}

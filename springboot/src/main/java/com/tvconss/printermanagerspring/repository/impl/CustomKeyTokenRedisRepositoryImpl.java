package com.tvconss.printermanagerspring.repository.impl;

import com.tvconss.printermanagerspring.entity.KeyTokenEntity;
import com.tvconss.printermanagerspring.repository.CustomKeyTokenRedisRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Repository
public class CustomKeyTokenRedisRepositoryImpl implements CustomKeyTokenRedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final Long accessTokenExpireTime;

    public CustomKeyTokenRedisRepositoryImpl(RedisTemplate<String, Object> redisTemplate,
                                             @Value("${jwt.access_token_expire_time}") Long accessTokenExpireTime) {
        this.redisTemplate = redisTemplate;
        this.accessTokenExpireTime = accessTokenExpireTime;
    }

    @Override
    public void createOrUpdateKeyToken(KeyTokenEntity keyToken) {
        // Sử dụng SessionCallback để đảm bao atomic operation
        redisTemplate.execute(new SessionCallback<Object>() {
            @Override
            public Object execute(@NotNull RedisOperations operations) throws DataAccessException {
                String key = "user:key_token:" + keyToken.getUserId() + ":" + keyToken.getJti();
                String publicKey = keyToken.getPublicKey();

                System.out.println("PUBLIC KEY IN REDIS: " + publicKey);

                Map<String, Object> keyTokenMap = Map.of(
                    "key", key,
                    "userId", keyToken.getUserId(),
                    "jti", keyToken.getJti(),
                    "publicKey", publicKey
                );

                operations.opsForHash().putAll(key, keyTokenMap);
                operations.expire(key, Duration.ofSeconds(accessTokenExpireTime));

                return null;
            }
        });

    }
}

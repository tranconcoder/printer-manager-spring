package com.tvconss.printermanagerspring.repository.impl;

import com.tvconss.printermanagerspring.entity.KeyTokenEntity;
import com.tvconss.printermanagerspring.repository.CustomKeyTokenRedisRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Repository
public class CustomKeyTokenRedisRepositoryImpl implements CustomKeyTokenRedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    public CustomKeyTokenRedisRepositoryImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void createOrUpdateKeyToken(KeyTokenEntity keyToken) {
        String key = "user:key_token:" + keyToken.getUserId() + ":" + keyToken.getJti();
        String publicKey = keyToken.getPublicKey();

        System.out.println("PUBLIC KEY IN REDIS: " + publicKey);

        Map<String, Object> keyTokenMap = Map.of(
                "key", key,
                "userId", keyToken.getUserId(),
                "jti", keyToken.getJti(),
                "publicKey", publicKey
        );
        this.redisTemplate.opsForHash()
                .putAll(key, keyTokenMap);
    }
}

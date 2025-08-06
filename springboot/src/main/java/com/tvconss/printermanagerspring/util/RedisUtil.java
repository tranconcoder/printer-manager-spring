package com.tvconss.printermanagerspring.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RedisUtil {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public Long getNextJti(Long userId) {
        String key = String.format("auth:key_token:%d:counter", userId);

        Long nextCounter = this.stringRedisTemplate.opsForValue().increment(key);

        return nextCounter;
    }

    public String getKeyTokenHashKey(Long userId, Long jti) {
        return String.format("auth:key_token:%d:%d", userId, jti);
    }
}

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
        String key = String.format("user:key_token:%d:counter", userId);

        return this.stringRedisTemplate.opsForValue().increment(key);
    }

    public String getKeyTokenKey(Long userId, Long jti) {
        return String.format("%d:%d", userId, jti);
    }
}

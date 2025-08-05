package com.tvconss.printermanagerspring.service.impl;

import com.tvconss.printermanagerspring.dto.internal.KeyToken;
import com.tvconss.printermanagerspring.service.RedisService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RedisServiceImpl implements RedisService {

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, Object> hashOperations;

    private Jackson2HashMapper hashMapper;

    public RedisServiceImpl(Jackson2HashMapper hashMapper) {
        this.hashMapper = hashMapper;
    }

    public void writeKeyTokenHash(String key, KeyToken keyToken) {

        Map<String, Object> keyTokenMap = hashMapper.toHash(keyToken);

        hashOperations.putAll(key, keyTokenMap);
    }

    public KeyToken loadKeyTokenHash(String key) {
        Map<String, Object> keyTokenMap = hashOperations.entries(key);

        return (KeyToken) hashMapper.fromHash(keyTokenMap);
    }

}

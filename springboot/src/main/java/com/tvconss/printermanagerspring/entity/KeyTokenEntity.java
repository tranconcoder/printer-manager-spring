package com.tvconss.printermanagerspring.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@RedisHash("user:key_token")
@Data
public class KeyTokenEntity {

    @Id
    private String key;

    private Long userId;
    private Long jti;
    private String publicKey;

    @TimeToLive(unit = TimeUnit.SECONDS)
    private Long ttl;
}

package com.tvconss.printermanagerspring.service.impl;

import com.tvconss.printermanagerspring.dto.internal.KeyToken;
import com.tvconss.printermanagerspring.service.KeyTokenService;
import com.tvconss.printermanagerspring.service.RedisService;
import com.tvconss.printermanagerspring.util.RedisUtil;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.util.Base64;
import java.util.UUID;

@Service
public class KeyTokenServiceImpl implements KeyTokenService {

    private RedisService redisService;

    public KeyTokenServiceImpl(RedisService redisService) {
        this.redisService = redisService;
    }

    public void createKeyToken(PublicKey publicKey, Long userId, UUID jitUUID) {
        String redisKey = RedisUtil.getKeyTokenHashKey(userId);
        String publicKeyStr = Base64.getEncoder().encodeToString(publicKey.getEncoded());

        KeyToken keyToken = new KeyToken();
        keyToken.setUserId(userId);
        keyToken.setPublicKey(publicKeyStr);
        keyToken.setJit(jitUUID.toString());


        this.redisService.writeKeyTokenHash(redisKey, keyToken);
    }

    public KeyToken getKeyTokenByUserId(Long userId) {
        String redisKey = String.format("auth:key_token:%d", userId);

        KeyToken keyToken = this.redisService.loadKeyTokenHash(redisKey);

        return  keyToken;
    }
}

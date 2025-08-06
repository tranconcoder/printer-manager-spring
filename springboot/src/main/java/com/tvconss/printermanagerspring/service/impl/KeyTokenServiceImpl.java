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
    private RedisUtil redisUtil ;

    public KeyTokenServiceImpl(RedisService redisService, RedisUtil redisUtil) {
        this.redisService = redisService;
        this.redisUtil = redisUtil;
    }

    public void createKeyToken(PublicKey publicKey, Long userId, Long jti) {
        String redisKey = this.redisUtil.getKeyTokenHashKey(userId, jti);
        String publicKeyStr = Base64.getEncoder().encodeToString(publicKey.getEncoded());

        KeyToken keyToken = new KeyToken();
        keyToken.setUserId(userId);
        keyToken.setPublicKey(publicKeyStr);
        keyToken.setJit(jti.toString());


        this.redisService.writeKeyTokenHash(redisKey, keyToken);
    }

    public KeyToken getKeyTokenByUserId(Long userId, Long jti) {
        String redisKey = this.redisUtil.getKeyTokenHashKey(userId, jti);

        KeyToken keyToken = this.redisService.loadKeyTokenHash(redisKey);

        return  keyToken;
    }
}

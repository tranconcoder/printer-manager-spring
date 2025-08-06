package com.tvconss.printermanagerspring.service.impl;

import com.tvconss.printermanagerspring.entity.KeyTokenEntity;
import com.tvconss.printermanagerspring.enums.ErrorCode;
import com.tvconss.printermanagerspring.exception.ErrorResponse;
import com.tvconss.printermanagerspring.repository.KeyTokenRedisRepository;
import com.tvconss.printermanagerspring.service.KeyTokenService;
import com.tvconss.printermanagerspring.util.RedisUtil;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.util.Base64;

@Service
public class KeyTokenServiceImpl implements KeyTokenService {

    private KeyTokenRedisRepository keyTokenRedisRepository;
    private RedisUtil redisUtil ;

    public KeyTokenServiceImpl(RedisUtil redisUtil, KeyTokenRedisRepository keyTokenRedisRepository) {
        this.redisUtil = redisUtil;
        this.keyTokenRedisRepository = keyTokenRedisRepository;
    }
    
    public void createKeyToken(PublicKey publicKey, Long userId, Long jti) {
        String redisKey = this.redisUtil.getKeyTokenHashKey(userId, jti);
        String publicKeyStr = Base64.getEncoder().encodeToString(publicKey.getEncoded());

        KeyTokenEntity keyToken = new KeyTokenEntity();
        keyToken.setKey(String.format("%d:%d", userId, jti));
        keyToken.setUserId(userId);
        keyToken.setPublicKey(publicKeyStr);
        keyToken.setJti(jti);

        KeyTokenEntity newKeyToken = this.keyTokenRedisRepository.save(keyToken);
        if (newKeyToken == null)  {
            throw new ErrorResponse(ErrorCode.AUTH_ERROR_INTERNAL);
        }
    }

//    public KeyToken getKeyTokenByUserId(Long userId, UUID jitUUID) {
//        String redisKey = this.redisUtil.getKeyTokenHashKey(userId, jitUUID);
//
//        KeyToken keyToken = this.redisService.loadKeyTokenHash(redisKey);
//
//        return  keyToken;
//    }
}

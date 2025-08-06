package com.tvconss.printermanagerspring.service;

import com.tvconss.printermanagerspring.dto.internal.KeyToken;

import java.security.PublicKey;
import java.util.UUID;

public interface KeyTokenService {

    public void createKeyToken(PublicKey publicKey, Long userId, Long jti);

    public KeyToken getKeyTokenByUserId(Long userId, Long jti);
}

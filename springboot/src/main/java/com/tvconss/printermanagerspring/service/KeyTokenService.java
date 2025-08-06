package com.tvconss.printermanagerspring.service;

import java.security.PublicKey;

public interface KeyTokenService {

    public void createKeyToken(PublicKey publicKey, Long userId, Long jti);

//    public KeyToken getKeyTokenByUserId(Long userId, Long jti);
}

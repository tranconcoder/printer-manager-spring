package com.tvconss.printermanagerspring.service;

import com.tvconss.printermanagerspring.dto.internal.KeyToken;

public interface RedisService {

    public void writeKeyTokenHash(String key, KeyToken keyToken);

    public KeyToken loadKeyTokenHash(String key);

}

package com.tvconss.printermanagerspring.repository;

import com.tvconss.printermanagerspring.entity.KeyTokenEntity;

public interface CustomKeyTokenRedisRepository {

    public void createOrUpdateKeyToken(KeyTokenEntity keyToken);

}

package com.tvconss.printermanagerspring.repository;

import com.tvconss.printermanagerspring.entity.KeyTokenEntity;
import org.springframework.data.repository.CrudRepository;

public interface KeyTokenRedisRepository extends CrudRepository<KeyTokenEntity, String> {
}

package com.yule.dashboard.redis.repository;

import com.yule.dashboard.redis.entities.RedisBaseUserInfoEntity;
import org.springframework.data.repository.CrudRepository;

public interface RedisUserRepository extends CrudRepository<RedisBaseUserInfoEntity, String> {
    RedisBaseUserInfoEntity findByLoginId(String loginId);
}

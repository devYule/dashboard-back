package com.yule.dashboard.my;

import com.yule.dashboard.redis.entities.RedisBaseUserInfoEntity;
import com.yule.dashboard.redis.repository.RedisUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
public class Sketchbook {

    @Autowired
    RedisUserRepository redisUserRepository;

    @Test
    @Rollback(value = false)
    void test() {
        RedisBaseUserInfoEntity userInfo = RedisBaseUserInfoEntity.builder()
                .loginId("test")
                .build();
        RedisBaseUserInfoEntity saved = redisUserRepository.save(userInfo);
        System.out.println("saved.getLoginId() = " + saved.getLoginId());
        RedisBaseUserInfoEntity result = redisUserRepository.findByLoginId("test");
        System.out.println("result = " + result);
        RedisBaseUserInfoEntity userInfo1 = redisUserRepository.findById(saved.getId()).get();
        System.out.println("userInfo1.getLoginId() = " + userInfo1.getLoginId());
        System.out.println(userInfo1.getId());
        System.out.println(userInfo1.getPk());

    }

}

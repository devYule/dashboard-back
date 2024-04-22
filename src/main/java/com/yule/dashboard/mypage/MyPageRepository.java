package com.yule.dashboard.mypage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yule.dashboard.entities.Site;
import com.yule.dashboard.mypage.model.MailCheckInfo;
import com.yule.dashboard.pbl.exception.ClientException;
import com.yule.dashboard.pbl.exception.ExceptionCause;
import com.yule.dashboard.pbl.exception.ServerException;
import com.yule.dashboard.pbl.utils.MailAuthenticationUtils;
import com.yule.dashboard.redis.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;

@Repository
public class MyPageRepository {

    private final RedisTemplate<String, String> redisTokenAndMailRepository;
    private final MailAuthenticationUtils mailAuthenticationUtils;
    private final ObjectMapper om;

    public MyPageRepository(@Qualifier("TokenAndMailHolder") RedisTemplate<String, String> redisTokenAndMailRepository,
                            MailAuthenticationUtils mailAuthenticationUtils,
                            ObjectMapper om) {
        this.redisTokenAndMailRepository = redisTokenAndMailRepository;
        this.mailAuthenticationUtils = mailAuthenticationUtils;
        this.om = om;
    }

    public String saveMailCode(String mail) {
        String code = mailAuthenticationUtils.sendAuthMail(mail);
        String key = RedisUtils.genMailCode();
        MailCheckInfo info = MailCheckInfo.builder()
                .mail(mail)
                .code(code)
                .build();

        String stringify;
        try {
            stringify = om.writeValueAsString(info);
        } catch (JsonProcessingException e) {
            throw new ServerException();
        }

        redisTokenAndMailRepository.opsForValue().set(key, stringify, Duration.ofMinutes(RedisUtils.mailTimeoutMinute));
        return key;
    }


    public MailCheckInfo getMailCheckInfo(String key) {
        ValueOperations<String, String> operation = redisTokenAndMailRepository.opsForValue();
        String findInfoJson = operation.get(key);
        if (findInfoJson == null) throw new ClientException(ExceptionCause.RETRY_SIGN_UP);
        MailCheckInfo findInfo;
        try {
            findInfo = om.readValue(findInfoJson, MailCheckInfo.class);
        } catch (JsonProcessingException e) {
            throw new ServerException();
        }
        if (findInfo.getCode() == null || findInfo.getMail() == null) throw new ClientException(ExceptionCause.RETRY_SIGN_UP);

        redisTokenAndMailRepository.delete(key);
        return findInfo;
    }

}

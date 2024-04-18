package com.yule.dashboard.user;

import com.yule.dashboard.entities.Users;
import com.yule.dashboard.entities.Widget;
import com.yule.dashboard.entities.enums.BaseState;
import com.yule.dashboard.pbl.exception.ClientException;
import com.yule.dashboard.pbl.exception.ExceptionCause;
import com.yule.dashboard.pbl.security.SecurityProperties;
import com.yule.dashboard.redis.entities.RedisBaseUserInfoEntity;
import com.yule.dashboard.redis.repository.RedisUserRepository;
import com.yule.dashboard.user.jparepo.UserJpaRepository;
import com.yule.dashboard.user.queryrepo.UserQueryRepository;
import com.yule.dashboard.widget.queryrepo.WidgetQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final UserJpaRepository userJpaRepository;
    private final UserQueryRepository userQueryRepository;
    private final RedisUserRepository redisUserRepository;
    private final WidgetQueryRepository widgetQueryRepository;
    private final RedisTemplate<String, String> redisTokenRepository;
    private final SecurityProperties properties;

    public Users findByLoginIdAndState(String userId) {
        return userJpaRepository.findByLoginIdAndState(userId, BaseState.ACTIVATED);
    }

    public String save(RedisBaseUserInfoEntity userInfo) {
        return redisUserRepository.save(userInfo).getId();
    }

    public RedisBaseUserInfoEntity findById(String id) {
        return redisUserRepository.findById(id).orElseThrow(() -> new ClientException(ExceptionCause.PW_NOT_EXISTS));
    }

    public void saveToken(String at, String rt) {
        ValueOperations<String, String> rep = redisTokenRepository.opsForValue();
        rep.set(at, rt, Duration.ofMillis(properties.getJwt().getRefreshTokenExpiry()));
    }

    public List<Widget> getAllWidgets(Long id, int page) {
        return widgetQueryRepository.findWidgetsByIdLimit(id, page);
    }

    public List<Users> checkSignupInfo(String loginId, String nick) {
        return userQueryRepository.checkSignupInfo(loginId, nick);

    }

    public int cntByMail(String mail) {
        return userQueryRepository.cntByMail(mail);
    }

    public String saveMailCode(String key, String code) {
        return redisUserRepository.findById(key).orElseThrow(() -> new ClientException(ExceptionCause.RETRY_SIGN_UP)).getId();
    }

    public RedisBaseUserInfoEntity findByKey(String key) {
        return redisUserRepository.findById(key).orElseThrow(() -> new ClientException(ExceptionCause.RETRY_SIGN_UP));
    }
}

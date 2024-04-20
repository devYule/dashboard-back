package com.yule.dashboard.user;

import com.yule.dashboard.entities.Users;
import com.yule.dashboard.entities.Widget;
import com.yule.dashboard.entities.enums.BaseState;
import com.yule.dashboard.pbl.exception.ClientException;
import com.yule.dashboard.pbl.exception.ExceptionCause;
import com.yule.dashboard.pbl.exception.ServerException;
import com.yule.dashboard.pbl.security.SecurityProperties;
import com.yule.dashboard.redis.entities.RedisBaseUserInfoEntity;
import com.yule.dashboard.redis.repository.RedisUserRepository;
import com.yule.dashboard.user.repositories.jparepo.UserJpaRepository;
import com.yule.dashboard.user.repositories.queryrepo.UserQueryRepository;
import com.yule.dashboard.widget.repositories.jparepo.WidgetJpaRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;

@Repository
public class UserRepository {
    private final UserJpaRepository userJpaRepository;
    private final UserQueryRepository userQueryRepository;
    private final RedisUserRepository redisUserRepository;
    private final WidgetJpaRepository widgetJpaRepository;

    private final RedisTemplate<String, String> redisTokenRepository;
    private final SecurityProperties properties;

    public UserRepository(
            UserJpaRepository userJpaRepository,
            UserQueryRepository userQueryRepository,
            RedisUserRepository redisUserRepository,
            WidgetJpaRepository widgetJpaRepository,
            @Qualifier("TokenHolder") RedisTemplate<String, String> redisTokenRepository,
            SecurityProperties properties) {
        this.userJpaRepository = userJpaRepository;
        this.userQueryRepository = userQueryRepository;
        this.redisUserRepository = redisUserRepository;
        this.widgetJpaRepository = widgetJpaRepository;
        this.redisTokenRepository = redisTokenRepository;
        this.properties = properties;
    }

    public Users findByLoginId(String userId) {
        return userJpaRepository.findByLoginId(userId);
    }

    public String save(RedisBaseUserInfoEntity userInfo) {
        return redisUserRepository.save(userInfo).getId();
    }

    public Users save(Users user) {
        return userJpaRepository.save(user);
    }


    public void saveToken(String at, String rt) {
        ValueOperations<String, String> rep = redisTokenRepository.opsForValue();
        rep.set(at, rt, Duration.ofMillis(properties.getJwt().getRefreshTokenExpiry()));
    }

    public List<Widget> getAllWidgets(Long id, BaseState state, int page) {
        return widgetJpaRepository.findByUserIdAndStateOffsetPageLimit(id, state, page);
    }

    public List<Users> checkSignupInfo(String loginId, String nick) {
        return userQueryRepository.checkSignupInfo(loginId, nick);

    }

    public int cntByMail(String mail) {
        return userQueryRepository.cntByMail(mail);
    }

    public String saveMailCode(String key, String code) {
        RedisBaseUserInfoEntity userInfo = redisUserRepository.findById(key).orElseThrow(() -> new ClientException(ExceptionCause.RETRY_SIGN_UP));
        userInfo.setCode(code);
        return userInfo.getId();
    }

    public RedisBaseUserInfoEntity findByKey(String key) {
        return redisUserRepository.findById(key).orElseThrow(() -> new ClientException(ExceptionCause.RETRY_SIGN_UP));
    }

    public String refreshToken(String at) {
        return redisTokenRepository.opsForValue().get(at);
    }

    public Users findById(Long id) {
        return userJpaRepository.findById(id).orElseThrow(ServerException::new);
    }

    public boolean existsByNick(String nick) {
        return userJpaRepository.existsByNick(nick);
    }
}

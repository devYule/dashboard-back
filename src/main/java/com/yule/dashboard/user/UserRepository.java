package com.yule.dashboard.user;

import com.yule.dashboard.entities.Users;
import com.yule.dashboard.entities.Widget;
import com.yule.dashboard.entities.enums.BaseState;
import com.yule.dashboard.pbl.exception.ClientException;
import com.yule.dashboard.pbl.exception.ExceptionCause;
import com.yule.dashboard.pbl.exception.ServerException;
import com.yule.dashboard.pbl.security.SecurityProperties;
import com.yule.dashboard.redis.entities.RedisBaseUserInfoEntity;
import com.yule.dashboard.redis.enums.RedisDataType;
import com.yule.dashboard.redis.repository.RedisUserRepository;
import com.yule.dashboard.redis.utils.RedisUtils;
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

    private final RedisTemplate<String, String> redisTokenAndMailRepository;
    private final SecurityProperties properties;

    public UserRepository(
            UserJpaRepository userJpaRepository,
            UserQueryRepository userQueryRepository,
            RedisUserRepository redisUserRepository,
            WidgetJpaRepository widgetJpaRepository,
            @Qualifier("TokenAndMailHolder") RedisTemplate<String, String> redisTokenAndMailRepository,
            SecurityProperties properties) {
        this.userJpaRepository = userJpaRepository;
        this.userQueryRepository = userQueryRepository;
        this.redisUserRepository = redisUserRepository;
        this.widgetJpaRepository = widgetJpaRepository;
        this.redisTokenAndMailRepository = redisTokenAndMailRepository;
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
        ValueOperations<String, String> rep = redisTokenAndMailRepository.opsForValue();
        rep.set(RedisDataType.TOKEN.getValue() + at, rt, Duration.ofMillis(properties.getJwt().getRefreshTokenExpiry()));
    }

    public List<Widget> getAllWidgets(Long id, BaseState state, int page) {
        return widgetJpaRepository.findByUserIdAndStateOffsetPageLimit(id, state, page);
    }

    public List<Users> checkSignupInfo(String loginId, String nick) {
        return userQueryRepository.checkSignupInfo(loginId, nick);

    }

    public boolean existsByMail(String mail) {
        return userJpaRepository.existsByMail(mail);
    }

    public String saveMailCode(String key, String code) {
        RedisBaseUserInfoEntity userInfo = redisUserRepository.findById(key).orElseThrow(() -> new ClientException(ExceptionCause.RETRY_SIGN_UP));
        // token and mail 에 key value 로 저장,
        // key 를 userInfo 에 저장.
        // key 에는 userInfo 키가 있고, code 는 사용자가 입력해야할 code 가 들어 있음.
        String mailKey = RedisUtils.genMailCode();
        redisTokenAndMailRepository.opsForValue().set(mailKey, code, Duration.ofMinutes(RedisUtils.mailTimeoutMinute));
        userInfo.setValidMailKey(mailKey);
        return userInfo.getId();
    }

    public RedisBaseUserInfoEntity findByKey(String key) {
        return redisUserRepository.findById(key).orElseThrow(() -> new ClientException(ExceptionCause.RETRY_SIGN_UP));
    }

    public String refreshToken(String at) {
        return redisTokenAndMailRepository.opsForValue().get(RedisDataType.TOKEN.getValue() + at);
    }

    public Users findById(Long id) {
        return userJpaRepository.findById(id).orElseThrow(ServerException::new);
    }

    public boolean existsByNick(String nick) {
        return userJpaRepository.existsByNick(nick);
    }

    public boolean checkMailCode(String validMailKey, String code) {
        String findCode = redisTokenAndMailRepository.opsForValue().get(validMailKey);
        if (findCode == null) throw new ClientException(ExceptionCause.RETRY_SIGN_UP);
        return findCode.equals(code);
    }

    public void deleteCache(String key) {
        redisUserRepository.deleteById(key);
    }

    public void delete(Users findUser) {
        userJpaRepository.delete(findUser);
    }
}

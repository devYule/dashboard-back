package com.yule.dashboard.user;

import com.yule.dashboard.entities.Users;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.RedisConnectionFailureException;
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

    private final RedisTemplate<String, String> redisTokenAndMailRepository;
    private final SecurityProperties properties;
    private final RedisUtils redisUtils;

    public UserRepository(
            UserJpaRepository userJpaRepository,
            UserQueryRepository userQueryRepository,
            RedisUserRepository redisUserRepository,
            @Qualifier("TokenAndMailHolder") RedisTemplate<String, String> redisTokenAndMailRepository,
            SecurityProperties properties,
            RedisUtils redisUtils) {
        this.userJpaRepository = userJpaRepository;
        this.userQueryRepository = userQueryRepository;
        this.redisUserRepository = redisUserRepository;
        this.redisTokenAndMailRepository = redisTokenAndMailRepository;
        this.properties = properties;
        this.redisUtils = redisUtils;
    }

    public Users findByLoginId(String userId) {
        return userJpaRepository.findByLoginIdAndState(userId, BaseState.ACTIVATED);
    }

    public String save(RedisBaseUserInfoEntity userInfo) {
        try {
            return redisUserRepository.save(userInfo).getId();
        } catch (RedisConnectionFailureException e) {
            throw new ServerException();
        }
    }

    public Users save(Users user) {
        return userJpaRepository.save(user);
    }


    public void saveToken(String at, String rt) {
        ValueOperations<String, String> rep = redisTokenAndMailRepository.opsForValue();
        rep.set(RedisDataType.TOKEN.getValue() + at, rt, Duration.ofMillis(properties.getJwt().getRefreshTokenExpiry()));
    }

//    public List<Widget> getAllWidgets(Long id, BaseState state, int page) {
//        return widgetJpaRepository.findByUserIdAndStateOffsetPageLimit(id, state, page);
//    }

    public List<Users> checkSignupInfo(String loginId, String nick) {
        return userQueryRepository.checkSignupInfo(loginId, nick);

    }

    public boolean existsByMail(String mail) {
        return userJpaRepository.existsByMailAndState(mail, BaseState.ACTIVATED);
    }

    public String saveMailCode(String code) {

        // token and mail 에 key value 로 저장,
        // key 를 userInfo 에 저장.
        // key 에는 userInfo 키가 있고, code 는 사용자가 입력해야할 code 가 들어 있음.
        String mailKey = RedisUtils.genMailCode();
        long mailTimeoutMillis = redisUtils.getMailTimeoutMillis();
        redisTokenAndMailRepository.opsForValue().set(mailKey, code, Duration.ofMillis(mailTimeoutMillis));

        return mailKey;
    }

    public RedisBaseUserInfoEntity findByKey(String key) {
        return redisUserRepository.findById(key).orElseThrow(() -> new ClientException(ExceptionCause.RETRY_SIGN_UP));
    }

    public String refreshToken(String at) {
        return redisTokenAndMailRepository.opsForValue().get(RedisDataType.TOKEN.getValue() + at);
    }

    public Users findById(Long id) {
        return userJpaRepository.findByIdAndState(id, BaseState.ACTIVATED).orElseThrow(ServerException::new);
    }

    public boolean existsByNick(String nick) {
        return userJpaRepository.existsByNickAndState(nick, BaseState.ACTIVATED);
    }

    public boolean checkMailCode(String validMailKey, String code) {
        String findCode = redisTokenAndMailRepository.opsForValue().get(validMailKey);
        if (findCode == null) throw new ClientException(ExceptionCause.RETRY_SIGN_UP);
        return findCode.equals(code);
    }

    public void deleteCache(String key) {
        redisUserRepository.deleteById(key);
    }



//    public Users findUserWithSitesById(Long id) {
//        return userQueryRepository.findUserWithSitesById(id);
//    }
}

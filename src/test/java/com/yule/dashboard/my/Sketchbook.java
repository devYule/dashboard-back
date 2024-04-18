package com.yule.dashboard.my;

import com.yule.dashboard.bookmark.BookmarkService;
import com.yule.dashboard.bookmark.jparepo.BookmarkJpaRepository;
import com.yule.dashboard.entities.BookMark;
import com.yule.dashboard.entities.Users;
import com.yule.dashboard.entities.Widget;
import com.yule.dashboard.entities.embeddable.UrlPath;
import com.yule.dashboard.entities.enums.BaseState;
import com.yule.dashboard.entities.enums.TrueOrFalse;
import com.yule.dashboard.entities.enums.WidgetSize;
import com.yule.dashboard.redis.entities.RedisBaseUserInfoEntity;
import com.yule.dashboard.redis.repository.RedisUserRepository;
import com.yule.dashboard.user.UserRepository;
import com.yule.dashboard.user.jparepo.UserJpaRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@SpringBootTest
public class Sketchbook {

    @Autowired
    RedisUserRepository redisUserRepository;
    @Autowired
    BookmarkJpaRepository bookmarkJpaRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EntityManager em;
    @Autowired
    UserJpaRepository userJpaRepository;

    Long userId;
    Users user;
    @BeforeEach
    @Rollback(value = false)
    void before() {
        Users saveUser = userRepository.save(Users.builder()
                .loginId("test")
                .pw("test")
                .nick("test")
                .state(BaseState.ACTIVATED)
                .build());
        userRepository.save(saveUser);
        userId = saveUser.getId();
        user = saveUser;

    }

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

    @Test
    @Rollback(value = false)
    @DisplayName("findByUserId check")
    void test2() {

        BookMark bookMark = new BookMark("test", new UrlPath("etst"), "seta");
        bookMark.setUser(user);
        BookMark saveBookmark = bookmarkJpaRepository.save(bookMark);
        bookmarkJpaRepository.flush();

        List<BookMark> findBookmarks = bookmarkJpaRepository.findByUserId(user.getId());
        System.out.println("findBookmarks.size() = " + findBookmarks.size());
        for (BookMark findBookmark : findBookmarks) {
            System.out.println("findBookmark.getMemo() = " + findBookmark.getMemo());
        }
        System.out.println("userJpaRepository.findByLoginIdAndState(saveUser.getLoginId(), BaseState.ACTIVATED) = " + userJpaRepository.findByLoginIdAndState(user.getLoginId(), BaseState.ACTIVATED).getLoginId());
    }

    @Test
    void checkNull() {

    }
}

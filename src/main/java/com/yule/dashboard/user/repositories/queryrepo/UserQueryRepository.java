package com.yule.dashboard.user.repositories.queryrepo;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yule.dashboard.entities.QUsers;
import com.yule.dashboard.entities.Users;
import com.yule.dashboard.entities.enums.BaseState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.yule.dashboard.entities.QUsers.*;

@Repository
@RequiredArgsConstructor
public class UserQueryRepository {

    private final JPAQueryFactory query;


    public List<Users> checkSignupInfo(String loginId, String nick) {
        return query.selectFrom(users)
                .where(users.loginId.eq(loginId).or(users.nick.eq(nick)))
                .fetch();
    }

    public int cntByMail(String mail) {
        Long cnt = query.select(users.count())
                .from(users)
                .where(users.mail.eq(mail))
                .fetchOne();
        return cnt == null ? 0 : cnt.intValue();
    }
}

package com.yule.dashboard.user.repositories.queryrepo;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yule.dashboard.entities.QSite;
import com.yule.dashboard.entities.QUsers;
import com.yule.dashboard.entities.Users;
import com.yule.dashboard.entities.enums.BaseState;
import com.yule.dashboard.mypage.model.AllUserInfoVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.yule.dashboard.entities.QSite.*;
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


//    public Users findUserWithSitesById(Long id) {
//        return query.selectFrom(users)
//                .leftJoin(site1).on(site1.user.id.eq(users.id).and(site1.state.eq(BaseState.ACTIVATED))).fetchJoin()
//                .where(users.id.eq(id))
//                .fetchOne();
//
//    }
}

package com.yule.dashboard.mypage.repositories.jparepo;

import com.yule.dashboard.entities.Site;
import com.yule.dashboard.entities.Users;
import com.yule.dashboard.entities.enums.BaseState;
import com.yule.dashboard.entities.enums.SiteType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SiteJpaRepository extends JpaRepository<Site, Long> {
    List<Site> findByUserIdAndState(Long id, BaseState state);

    Site findByUserAndStateAndSite(Users user, BaseState baseState, SiteType site);

    Site findByUserIdAndStateAndSite(Long userId, BaseState baseState, SiteType site);
}

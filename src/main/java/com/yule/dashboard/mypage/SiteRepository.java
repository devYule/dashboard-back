package com.yule.dashboard.mypage;

import com.yule.dashboard.entities.Site;
import com.yule.dashboard.entities.Users;
import com.yule.dashboard.entities.enums.BaseState;
import com.yule.dashboard.entities.enums.SiteType;
import com.yule.dashboard.mypage.repositories.jparepo.SiteJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SiteRepository {

    private final SiteJpaRepository siteJpaRepository;

    public List<Site> findByUserIdAndState(Long id, BaseState state) {
        return siteJpaRepository.findByUserIdAndState(id, state);
    }

    public Site findByUserAndStateAndSite(Users user, BaseState baseState, SiteType site) {
        return siteJpaRepository.findByUserAndStateAndSite(user, baseState, site);
    }

    public Site findByUserIdAndStateAndSite(Long userId, BaseState baseState, SiteType site) {
        return siteJpaRepository.findByUserIdAndStateAndSite(userId, baseState, site);
    }

    public Site save(Site saveSite) {
        return siteJpaRepository.save(saveSite);
    }

    public Site findByUserAndSite(Users user, SiteType site) {
        return siteJpaRepository.findByUserAndSite(user, site);
    }
}

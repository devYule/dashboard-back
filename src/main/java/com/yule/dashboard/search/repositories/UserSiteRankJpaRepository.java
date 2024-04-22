package com.yule.dashboard.search.repositories;

import com.yule.dashboard.entities.UserSiteRank;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSiteRankJpaRepository extends JpaRepository<UserSiteRank, Long> {
    List<UserSiteRank> findByUserId(Long id, Sort count);
}

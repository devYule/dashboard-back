package com.yule.dashboard.search;

import com.yule.dashboard.search.repositories.UserSiteRankJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserSiteRankRepository {

    private final UserSiteRankJpaRepository userSiteRankJpaRepository;

    public List<Integer> findSiteNumByUserId(Long id) {
        return userSiteRankJpaRepository.findByUserId(id, Sort.by(Sort.Direction.DESC, "count"))
                .stream().map(r -> r.getSite().getValue()).toList();
    }
}

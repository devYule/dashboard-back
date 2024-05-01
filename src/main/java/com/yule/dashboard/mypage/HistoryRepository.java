package com.yule.dashboard.mypage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yule.dashboard.entities.History;
import com.yule.dashboard.entities.Users;
import com.yule.dashboard.entities.enums.HistoryType;
import com.yule.dashboard.mypage.repositories.jparepo.HistoryJpaRepository;
import com.yule.dashboard.pbl.aop.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class HistoryRepository {

    private final HistoryJpaRepository historyJpaRepository;

    public History findFirstByUserAndType(Users user, HistoryType historyType, Sort sort) {
        return historyJpaRepository.findFirstByUserAndType(user, historyType, sort);
    }

    public History save(History history) {
        return historyJpaRepository.save(history);
    }

    @Retry
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public History saveHistory(Users user, HistoryType type, String value) {
        return saveHistory(user, null, type, value);
    }

    @Retry
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public History saveHistory(Users user, History prevHistory, HistoryType type, String value) {
        return save(History.builder()
                .user(user)
                .prev(prevHistory)
                .type(type)
                .value(value)
                .build());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public History findPrevHistory(Users user, HistoryType type) {
        return historyJpaRepository.findFirstByUserAndType(user, type, Sort.by(Sort.Direction.DESC, "id"));
    }
}

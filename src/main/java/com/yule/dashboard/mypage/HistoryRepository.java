package com.yule.dashboard.mypage;

import com.yule.dashboard.entities.History;
import com.yule.dashboard.entities.Users;
import com.yule.dashboard.entities.enums.HistoryType;
import com.yule.dashboard.mypage.repositories.jparepo.HistoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class HistoryRepository {

    private final HistoryJpaRepository historyJpaRepository;

    public History findFirstByUserAndType(Users user, HistoryType historyType, Sort sort) {

        return historyJpaRepository.findFirstByUserAndType(user, historyType, sort);
    }

    public History save(History history) {
        return historyJpaRepository.save(history);
    }
}

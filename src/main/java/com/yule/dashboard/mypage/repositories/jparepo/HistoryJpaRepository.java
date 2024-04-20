package com.yule.dashboard.mypage.repositories.jparepo;

import com.yule.dashboard.entities.History;
import com.yule.dashboard.entities.Users;
import com.yule.dashboard.entities.enums.HistoryType;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryJpaRepository extends JpaRepository<History, Long> {
    History findFirstByUserAndType(Users user, HistoryType historyType, Sort sort);
}

package com.yule.dashboard.widget.jparepo;

import com.yule.dashboard.entities.Widget;
import com.yule.dashboard.entities.enums.BaseState;
import com.yule.dashboard.widget.queryrepo.WidgetQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WidgetJpaRepository extends JpaRepository<Widget, Long>, WidgetQueryRepository {

    Optional<Widget> findByIdAndUserIdAndState(Long id, Long userId, BaseState baseState);


    List<Widget> findByUserIdAndState(Long id, BaseState baseState);
}

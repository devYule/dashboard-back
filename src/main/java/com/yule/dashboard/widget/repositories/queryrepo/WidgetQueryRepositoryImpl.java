package com.yule.dashboard.widget.repositories.queryrepo;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yule.dashboard.entities.Widget;
import com.yule.dashboard.entities.enums.BaseState;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.yule.dashboard.entities.QWidget.*;

@Repository
@RequiredArgsConstructor
public class WidgetQueryRepositoryImpl implements WidgetQueryRepository {

    private final JPAQueryFactory query;
    @Value("${limit.widget}")
    private int widgetLimit;

    @Override
    public List<Widget> findByUserIdAndStateOffsetPageLimit(Long id, BaseState state, int page) {
        return query.selectFrom(widget)
                .where(widget.user.id.eq(id).and(widget.state.eq(state)))
                .offset((long) (page - 1) * widgetLimit)
                .limit(widgetLimit)
                .fetch();

    }

}

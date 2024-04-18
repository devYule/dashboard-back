package com.yule.dashboard.widget.queryrepo;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yule.dashboard.entities.Widget;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.yule.dashboard.entities.QWidget.*;

@Repository
@RequiredArgsConstructor
public class WidgetQueryRepository {

    private final JPAQueryFactory query;
    @Value("${limit.widget}")
    private int widgetLimit;

    public List<Widget> findWidgetsByIdLimit(Long id, int page) {
        return query.selectFrom(widget)
                .where(widget.user.id.eq(id))
                .offset((long) (page - 1) * widgetLimit)
                .limit(widgetLimit)
                .fetch();

    }

}

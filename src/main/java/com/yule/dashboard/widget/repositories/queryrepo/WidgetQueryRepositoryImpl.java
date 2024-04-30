package com.yule.dashboard.widget.repositories.queryrepo;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yule.dashboard.entities.QBookmarkScreenShot;
import com.yule.dashboard.entities.QWidget;
import com.yule.dashboard.entities.Widget;
import com.yule.dashboard.entities.enums.BaseState;
import com.yule.dashboard.widget.model.data.resp.WidgetData;
import com.yule.dashboard.widget.model.data.resp.WidgetDataDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.yule.dashboard.entities.QBookmark.bookmark;
import static com.yule.dashboard.entities.QBookmarkScreenShot.*;
import static com.yule.dashboard.entities.QWidget.widget;


@Repository
@RequiredArgsConstructor
public class WidgetQueryRepositoryImpl implements WidgetQueryRepository {

    private final JPAQueryFactory query;
    @Value("${limit.widget}")
    private int widgetLimit;

    @Override
    public List<Widget> findByUserIdAndStateOffsetPageLimitDesc(Long id, BaseState state, int page) {
        return query.selectFrom(widget).join(bookmark).on(widget.bookmark.id.eq(bookmark.id)).fetchJoin()
                .where(widget.user.id.eq(id).and(widget.state.eq(state)))
                .offset((long) (page - 1) * widgetLimit)
                .limit(widgetLimit)
                .orderBy(widget.id.desc())
                .fetch();

    }

    @Override
    public List<WidgetData> findWidgetInfo(Long id, BaseState baseState, int page) {

        return query.select(Projections.bean(WidgetDataDto.class,
                        widget.id.as("id"),
                        widget.width.as("width"),
                        widget.height.as("height"),
                        widget.url.as("url"),
                        widget.bookmark.title.as("title"),
                        widget.bookmark.memo.as("memo"),
                        bookmarkScreenShot.shot.as("shot")))
                .from(widget).leftJoin(bookmark).on(widget.bookmark.id.eq(bookmark.id))
                .leftJoin(bookmarkScreenShot).on(bookmarkScreenShot.bookmark.id.eq(bookmark.id))
                .where(widget.user.id.eq(id).and(widget.state.eq(baseState)))
                .orderBy(widget.id.desc())
                .offset((long) (page - 1) * widgetLimit)
                .limit(widgetLimit)
                .fetch()
                .stream().map(dto -> new WidgetData(dto.getId(), dto.getWidth().getValue(),
                        dto.getHeight().getValue(),
                        dto.getUrl(), dto.getTitle(), dto.getMemo(), dto.getShot())).toList();

    }

}

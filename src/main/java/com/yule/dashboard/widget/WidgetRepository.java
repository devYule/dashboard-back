package com.yule.dashboard.widget;

import com.yule.dashboard.entities.Bookmark;
import com.yule.dashboard.entities.Widget;
import com.yule.dashboard.entities.enums.BaseState;
import com.yule.dashboard.pbl.exception.ClientException;
import com.yule.dashboard.pbl.exception.ExceptionCause;
import com.yule.dashboard.widget.model.data.resp.WidgetData;
import com.yule.dashboard.widget.repositories.jparepo.WidgetJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class WidgetRepository {
    private final WidgetJpaRepository widgetJpaRepository;
    @Value("${limit.widget}")
    private int limitWidget;

    public Widget save(Widget widget) {
        return widgetJpaRepository.save(widget);
    }

    public List<Widget> findByUserIdAndStateOffsetPageLimitDesc(Long id, BaseState state, int page) {
        return widgetJpaRepository.findByUserIdAndStateOffsetPageLimitDesc(id, state, page);

    }

    public Widget findByIdAndUserIdAndState(Long id, Long userId, BaseState state) {
        return widgetJpaRepository.findByIdAndUserIdAndState(id, userId, state).orElseThrow(() -> new ClientException(ExceptionCause.PRIMARY_KEY_IS_NOT_VALID));
    }


    public List<Widget> findByUserIdAndState(Long id, BaseState baseState) {
        return widgetJpaRepository.findByUserIdAndState(id, baseState);
    }

    public List<Widget> findByBookMarkAndState(Bookmark findBookmark, BaseState baseState) {
        return widgetJpaRepository.findByBookmarkAndState(findBookmark, baseState);
    }

    public List<WidgetData> findWidgetInfo(Long id, BaseState baseState) {
        return widgetJpaRepository.findWidgetInfo(id, baseState);
    }
}

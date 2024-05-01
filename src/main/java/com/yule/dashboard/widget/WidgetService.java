package com.yule.dashboard.widget;

import com.yule.dashboard.bookmark.BookmarkRepository;
import com.yule.dashboard.bookmark.BookmarkShotRepository;
import com.yule.dashboard.entities.Bookmark;
import com.yule.dashboard.entities.BookmarkScreenShot;
import com.yule.dashboard.entities.Users;
import com.yule.dashboard.entities.Widget;
import com.yule.dashboard.entities.enums.BaseState;
import com.yule.dashboard.entities.enums.TrueOrFalse;
import com.yule.dashboard.entities.enums.WidgetSize;
import com.yule.dashboard.entities.enums.WidgetType;
import com.yule.dashboard.pbl.BaseResponse;
import com.yule.dashboard.pbl.security.SecurityFacade;
import com.yule.dashboard.pbl.utils.LogicUtils;
import com.yule.dashboard.user.UserRepository;
import com.yule.dashboard.widget.model.data.req.WidgetAddData;
import com.yule.dashboard.widget.model.data.req.WidgetPatchData;
import com.yule.dashboard.widget.model.data.resp.AddWidgetData;
import com.yule.dashboard.widget.model.data.resp.WidgetData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WidgetService {

    private final WidgetRepository widgetRepository;
    private final UserRepository userRepository;
    private final BookmarkShotRepository bookmarkShotRepository;
    private final SecurityFacade facade;

    public AddWidgetData addWidget(WidgetAddData data) {
        Users findUser = userRepository.findById(facade.getId());
        BookmarkScreenShot findBookmarkShot = bookmarkShotRepository.findByBookmarkId(data.bookmarkId());
        Bookmark findBookmark = findBookmarkShot.getBookmark();
        Widget widget = new Widget(
                WidgetSize.getByValue(data.width()), WidgetSize.getByValue(data.height()),
                data.url(),
                WidgetType.BOOKMARK, data.type() == 0 ? findBookmark : null);
        widget.setUser(findUser);

        return new AddWidgetData(widgetRepository.save(widget).getId(), findBookmarkShot.getShot());

    }

    @Transactional
    public List<WidgetData> getAllWidgets() {

        return widgetRepository.findWidgetInfo(facade.getId(), BaseState.ACTIVATED);

    }

    public BaseResponse patchWidget(WidgetPatchData data) {
        LogicUtils.ifAllNullThrow(data.order(), data.width(), data.height(), data.url(), data.isShown());

        Widget findWidget = widgetRepository.findByIdAndUserIdAndState(data.id(), facade.getId(), BaseState.ACTIVATED);
        return BaseResponse.builder()
                .value(findWidget.setIfNotNullData(
//                        data.order(),
                        data.width(),
                        data.height(),
                        data.url()
//                        data.isShown()
                ).getId()).build();
    }

    @Transactional
    public BaseResponse removeWidget(Long id) {

        Widget findWidget = widgetRepository.findByIdAndUserIdAndState(id, facade.getId(), BaseState.ACTIVATED);
        findWidget.setState(BaseState.DEACTIVATED);
        return BaseResponse.builder().value(findWidget.getId()).build();
    }

    @Transactional
    public BaseResponse removeAllWidget() {
        List<Widget> findWidgets = widgetRepository.findByUserIdAndState(facade.getId(), BaseState.ACTIVATED);
        findWidgets.forEach(w -> w.setState(BaseState.DEACTIVATED));
        return BaseResponse.builder().value((long) findWidgets.size()).build();
    }
}

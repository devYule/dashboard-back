package com.yule.dashboard.widget;

import com.yule.dashboard.bookmark.BookmarkRepository;
import com.yule.dashboard.entities.BookMark;
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
    private final BookmarkRepository bookmarkRepository;
    private final SecurityFacade facade;

    public BaseResponse addWidget(WidgetAddData data) {
        Users findUser = userRepository.findById(facade.getId());
        BookMark findBookmark = bookmarkRepository.findByIdAndStateAndUserId(data.bookmarkId(), findUser.getId());
        Widget widget = new Widget(data.order(), WidgetSize.getByValue(data.width()), WidgetSize.getByValue(data.height()),
                data.url(), TrueOrFalse.getByValue(data.isShown()), data.type() == 0 ? WidgetType.BOOKMARK :
                WidgetType.UTILS, data.type() == 0 ? findBookmark : null);
        widget.setUser(findUser);

        return BaseResponse.builder().value(widgetRepository.save(widget).getId()).build();

    }

    @Transactional
    public List<WidgetData> getAllWidgets(int page) {
        return widgetRepository.findByUserIdAndStateOffsetPageLimit(facade.getId(), BaseState.ACTIVATED, page)
                .stream()
                .map(w -> new WidgetData(
                        w.getId(), w.getOrder(), w.getWidth().getValue(),
                        w.getHeight().getValue(),
                        w.getUrl(),
                        w.getIsShown().getValue(),
                        w.getBookmark().getTitle(),
                        w.getBookmark().getMemo())).toList();
    }

    public BaseResponse patchWidget(WidgetPatchData data) {
        LogicUtils.ifAllNullThrow(data.order(), data.width(), data.height(), data.url(), data.isShown());

        Widget findWidget = widgetRepository.findByIdAndUserIdAndState(data.id(), facade.getId(), BaseState.ACTIVATED);
        return BaseResponse.builder()
                .value(findWidget.setIfNotNullData(
                        data.order(),
                        data.width(),
                        data.height(),
                        data.url(),
                        data.isShown()
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

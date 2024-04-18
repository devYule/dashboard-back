package com.yule.dashboard.widget;

import com.yule.dashboard.entities.Users;
import com.yule.dashboard.entities.Widget;
import com.yule.dashboard.entities.embeddable.UrlPath;
import com.yule.dashboard.entities.enums.BaseState;
import com.yule.dashboard.entities.enums.TrueOrFalse;
import com.yule.dashboard.entities.enums.WidgetSize;
import com.yule.dashboard.pbl.BaseResponse;
import com.yule.dashboard.pbl.security.SecurityFacade;
import com.yule.dashboard.pbl.utils.LogicUtils;
import com.yule.dashboard.user.UserRepository;
import com.yule.dashboard.widget.model.data.req.WidgetAddData;
import com.yule.dashboard.widget.model.data.req.WidgetPatchData;
import com.yule.dashboard.widget.model.data.resp.WidgetData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WidgetService {

    private final WidgetRepository widgetRepository;
    private final UserRepository userRepository;
    private final SecurityFacade facade;

    public BaseResponse addWidget(WidgetAddData data) {
        Users findUser = userRepository.findByIdAndState(facade.getId(), BaseState.ACTIVATED);
        Widget widget = new Widget(data.order(), WidgetSize.getByValue(data.width()), WidgetSize.getByValue(data.height()),
                new UrlPath(data.url()), TrueOrFalse.getByValue(data.isShown()));
        widget.setUser(findUser);

        return BaseResponse.builder().value(widgetRepository.save(widget).getId()).build();

    }

    public List<WidgetData> getAllWidgets(int page) {
        return widgetRepository.findByUserIdAndStateOffsetPageLimit(facade.getId(), BaseState.ACTIVATED, page)
                .stream()
                .map(w -> new WidgetData(
                        w.getId(), w.getOrder(), w.getWidth().getValue(),
                        w.getHeight().getValue(),
                        w.getUrl().getUrl(),
                        w.getIsShown().getValue())).toList();
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

    public BaseResponse removeWidget(Long id) {

        Widget findWidget = widgetRepository.findByIdAndUserIdAndState(id, facade.getId(), BaseState.ACTIVATED);
        findWidget.setState(BaseState.DEACTIVATED);
        return BaseResponse.builder().value(findWidget.getId()).build();
    }

    public BaseResponse removeAllWidget() {
        List<Widget> findWidgets = widgetRepository.findByUserIdAndState(facade.getId(), BaseState.ACTIVATED);
        findWidgets.forEach(w -> w.setState(BaseState.DEACTIVATED));
        return BaseResponse.builder().value((long) findWidgets.size()).build();
    }
}

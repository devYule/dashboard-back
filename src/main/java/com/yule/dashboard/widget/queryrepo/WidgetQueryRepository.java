package com.yule.dashboard.widget.queryrepo;

import com.yule.dashboard.entities.Widget;
import com.yule.dashboard.entities.enums.BaseState;

import java.util.List;

public interface WidgetQueryRepository {
    List<Widget> findByUserIdAndStateOffsetPageLimit(Long id, BaseState state, int page);
}

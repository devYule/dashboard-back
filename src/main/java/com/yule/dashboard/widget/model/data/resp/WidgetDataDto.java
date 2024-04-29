package com.yule.dashboard.widget.model.data.resp;

import com.yule.dashboard.entities.enums.TrueOrFalse;
import com.yule.dashboard.entities.enums.WidgetSize;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WidgetDataDto {
    private Long id;
    private int order;
    private WidgetSize width;
    private WidgetSize height;
    private String url;
    private TrueOrFalse isShown;
    private String title;
    private String memo;
    private String shot;

}

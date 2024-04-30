package com.yule.dashboard.widget.model.data.resp;

import com.yule.dashboard.entities.enums.TrueOrFalse;
import com.yule.dashboard.entities.enums.WidgetSize;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WidgetDataDto {
    private Long id;
    private WidgetSize width;
    private WidgetSize height;
    private String url;
    private String title;
    private String memo;
    private String shot;

}

package com.yule.dashboard.user.model.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WidgetVo {
    private Long id;
    private int order;
    private int width;
    private int height;
    private String url;
    private int isShown;
}

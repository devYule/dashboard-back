package com.yule.dashboard.entities.enums;

import lombok.Getter;

@Getter
public enum SiteType {
    NAVER(0), GOOGLE(1), KAKAO(2),YAHOO(3), BAIDU(4), BING(5), COCCOC(6), DUCKDUCKGO(7), YANDEX(8);
    private final int value;

    SiteType(int value) {
        this.value = value;
    }
}

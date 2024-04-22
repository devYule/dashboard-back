package com.yule.dashboard.entities.enums;

import com.yule.dashboard.pbl.exception.ClientException;
import com.yule.dashboard.pbl.exception.ExceptionCause;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum SiteType {
    NAVER(0), GOOGLE(1), KAKAO(2), YAHOO(3), BAIDU(4), BING(5), COCCOC(6), DUCKDUCKGO(7), YANDEX(8);
    private final int value;

    SiteType(int value) {
        this.value = value;
    }

    public static SiteType getByValue(int value) {
        return Arrays.stream(SiteType.values())
                .filter(v -> v.getValue() == value)
                .findFirst()
                .orElseThrow(() -> new ClientException(ExceptionCause.REQUEST_VALUE_IS_NOT_VALID));
    }
}

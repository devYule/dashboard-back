package com.yule.dashboard.entities.enums;

import com.yule.dashboard.pbl.exception.ServerException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum WidgetSize {
    ONE(1), TWO(2);
    private final int value;

    WidgetSize(int value) {
        this.value = value;
    }

    public static WidgetSize getByValue(int value) {
        return Arrays.stream(WidgetSize.values()).filter(w -> w.getValue() == value).findFirst().orElseThrow(ServerException::new);
    }
}

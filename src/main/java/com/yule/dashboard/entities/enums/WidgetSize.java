package com.yule.dashboard.entities.enums;

import lombok.Getter;

@Getter
public enum WidgetSize {
    ONE(1), TWO(0);
    private final int value;

    WidgetSize(int value) {
        this.value = value;
    }
}

package com.yule.dashboard.entities.enums;

import lombok.Getter;

@Getter
public enum UtilType {
    WATCH(1);
    private final int value;

    UtilType(int value) {
        this.value = value;
    }
}

package com.yule.dashboard.entities.enums;

import lombok.Getter;

@Getter
public enum TrueOrFalse {
    TRUE(1), FALSE(0);
    private final int value;

    TrueOrFalse(int value) {
        this.value = value;
    }
}

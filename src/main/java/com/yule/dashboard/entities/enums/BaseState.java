package com.yule.dashboard.entities.enums;

import lombok.Getter;

@Getter
public enum BaseState {
    ACTIVATED(1), DEACTIVATED(0);
    private final int value;

    BaseState(int value) {
        this.value = value;
    }

}

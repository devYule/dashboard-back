package com.yule.dashboard.entities.enums;

import lombok.Getter;

@Getter
public enum SearchbarStyle {
    ROUND(1), SQUARE(2), LINE(3);

    private final int value;


    SearchbarStyle(int value) {
        this.value = value;
    }
}

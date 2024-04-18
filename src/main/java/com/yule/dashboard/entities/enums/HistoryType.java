package com.yule.dashboard.entities.enums;

import lombok.Getter;

@Getter
public enum HistoryType {

    PW(1), MAIL(2), PIC(3), SEARCHBAR(4), STATE(5);

    private final int value;

    HistoryType(int value) {
        this.value = value;
    }

}

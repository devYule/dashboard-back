package com.yule.dashboard.entities.enums;

import lombok.Getter;

@Getter
public enum HistoryType {

    PW(1), MAIL(2), PIC(3), NICK(4), SEARCHBAR(5), WITHDRAW(6);

    private final int value;

    HistoryType(int value) {
        this.value = value;
    }

}

package com.yule.dashboard.entities.enums;

import lombok.Getter;

@Getter
public enum HistoryType {

    WITHDRAW(0), PW(1), MAIL(2), PIC(3),
    NICK(4), SEARCHBAR(5), SITE(6), SEARCH(7),
    ;

    private final int value;

    HistoryType(int value) {
        this.value = value;
    }

}

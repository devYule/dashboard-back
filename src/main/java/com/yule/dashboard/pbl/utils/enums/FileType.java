package com.yule.dashboard.pbl.utils.enums;

import lombok.Getter;

@Getter
public enum FileType {
    PIC("pic");
    private final String value;

    FileType(String value) {
        this.value = value;
    }
}

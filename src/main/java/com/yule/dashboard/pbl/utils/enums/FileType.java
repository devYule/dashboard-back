package com.yule.dashboard.pbl.utils.enums;

import lombok.Getter;

@Getter
public enum FileType {
    PIC("pic"), SHOT("shot");
    private final String value;

    FileType(String value) {
        this.value = value;
    }
}

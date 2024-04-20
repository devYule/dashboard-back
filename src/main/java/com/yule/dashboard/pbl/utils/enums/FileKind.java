package com.yule.dashboard.pbl.utils.enums;

import lombok.Getter;

@Getter
public enum FileKind {
    PROFILE_PIC("prof");
    private final String value;

    FileKind(String value) {
        this.value = value;
    }
}

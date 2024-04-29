package com.yule.dashboard.pbl.utils.enums;

import lombok.Getter;

@Getter
public enum FileCategory {
    USER("user"), BOOKMARK("bookmark");

    private final String value;
    FileCategory(String value) {
        this.value = value;
    }
}

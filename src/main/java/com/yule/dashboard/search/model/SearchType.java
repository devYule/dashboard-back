package com.yule.dashboard.search.model;

import com.yule.dashboard.pbl.exception.ClientException;
import com.yule.dashboard.pbl.exception.ExceptionCause;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum SearchType {
    JSOUP(0, "fast"), SELENIUM(1, "enough");

    private int value;
    private String type;

    SearchType(int value, String type) {
        this.value = value;
        this.type = type;
    }

    public static SearchType getByValue(int value) {
        return Arrays.stream(SearchType.values())
                .filter(s -> s.getValue() == value)
                .findFirst()
                .orElseThrow(() -> new ClientException(ExceptionCause.TYPE_ERROR));
    }
}

package com.yule.dashboard.entities.enums;

import com.yule.dashboard.pbl.exception.ServerException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum TrueOrFalse {
    TRUE(1), FALSE(0);
    private final int value;

    TrueOrFalse(int value) {
        this.value = value;
    }

    public static TrueOrFalse getByValue(int value) {
        return Arrays.stream(TrueOrFalse.values()).filter(t -> t.getValue() == value).findFirst().orElseThrow(ServerException::new);
    }
}

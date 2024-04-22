package com.yule.dashboard.redis.enums;

import lombok.Getter;

@Getter
public enum RedisDataType {
    MAIL("m:"), TOKEN("tk:");
    private final String value;

    RedisDataType(String value) {
        this.value = value;
    }
}

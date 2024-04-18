package com.yule.dashboard.redis.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public record RedisRTData(String rt) {
}

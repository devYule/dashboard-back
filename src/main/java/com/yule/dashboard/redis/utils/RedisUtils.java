package com.yule.dashboard.redis.utils;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public abstract class RedisUtils {

    private static final String pattern = "yyyyMMddHHmmssSSSSSS";
    @Value("${times.timeout.mail-minute}")
    public static long mailTimeoutMinute;

    public static String genMailCode() {
        String format = DateTimeFormatter.ofPattern(pattern).format(LocalDateTime.now());
        String uuid = UUID.randomUUID().toString();
        return format + uuid.substring(uuid.indexOf("-"), uuid.indexOf("-") + 8);
    }
}

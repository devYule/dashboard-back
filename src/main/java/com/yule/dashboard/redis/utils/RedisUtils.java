package com.yule.dashboard.redis.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
@Component
@Getter
public class RedisUtils {

    private static final String pattern = "yyyyMMddHHmmssSSSSSS";
    @Value("${times.timeout.mail-millis}")
    private long mailTimeoutMillis;

    public static String genMailCode() {
        String format = DateTimeFormatter.ofPattern(pattern).format(LocalDateTime.now());
        String uuid = UUID.randomUUID().toString();
        return format + uuid.substring(uuid.indexOf("-"), uuid.indexOf("-") + 8);
    }
}

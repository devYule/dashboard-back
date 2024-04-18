package com.yule.dashboard.pbl.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "app")
public class SecurityProperties {
    private final Jwt jwt = new Jwt();

    @Getter
    @Setter
    public static class Jwt {
        private String secret;
        private String header;
        private String type;
        private Long accessTokenExpiry;
        private Long refreshTokenExpiry;

    }
}

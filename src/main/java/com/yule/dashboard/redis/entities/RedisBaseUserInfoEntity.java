package com.yule.dashboard.redis.entities;

import com.yule.dashboard.entities.enums.SearchbarStyle;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "base", timeToLive = 600)
public class RedisBaseUserInfoEntity {
    @Id
    private String id;
    private Long pk;
    private String loginId;
    private String pw;
    private String nick;
    private String pic;
    private SearchbarStyle searchbar;

    private String validMailKey;
    private String mail;

}

package com.yule.dashboard.entities.embeddable;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class UrlPath {

    private String url;

}

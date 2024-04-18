package com.yule.dashboard.entities;

import com.yule.dashboard.entities.embeddable.UrlPath;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@DiscriminatorValue("bookmark")
public class BookMark extends Data {
    private String title;
    private UrlPath url;
    private String memo;
}

package com.yule.dashboard.entities;

import com.yule.dashboard.entities.enums.TrueOrFalse;
import com.yule.dashboard.entities.enums.WidgetSize;
import com.yule.dashboard.entities.enums.WidgetType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@DiscriminatorValue("widget")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
public class Widget extends Data {

    @Enumerated(EnumType.STRING)
    private WidgetSize width;
    @Enumerated(EnumType.STRING)
    private WidgetSize height;

    @Column(length = 2147483647)
    private String url;
    @Enumerated(EnumType.STRING)
    private WidgetType type = WidgetType.BOOKMARK;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookmark_id")
    private Bookmark bookmark;

    public Widget setIfNotNullData(
            Integer width,
            Integer height,
            String url
    ) {
        if (width != null) {
            this.width = WidgetSize.getByValue(width);
        }
        if (height != null) {
            this.height = WidgetSize.getByValue(height);
        }
        if (url != null) {
            this.url = url;
        }
        return this;
    }

}

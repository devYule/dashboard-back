package com.yule.dashboard.entities;

import com.yule.dashboard.entities.embeddable.UrlPath;
import com.yule.dashboard.entities.enums.TrueOrFalse;
import com.yule.dashboard.entities.enums.WidgetSize;
import com.yule.dashboard.entities.enums.WidgetType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@DiscriminatorValue("widget")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
public class Widget extends Data {

    @Column(name = "_order")
    private int order;
    @Enumerated(EnumType.STRING)
    private WidgetSize width;
    @Enumerated(EnumType.STRING)
    private WidgetSize height;
    @Embedded
    private UrlPath url;
    @Enumerated(EnumType.STRING)
    private TrueOrFalse isShown;
    @Enumerated(EnumType.STRING)
    private WidgetType type = WidgetType.BOOKMARK;

    public Widget setIfNotNullData(
            Integer order,
            Integer width,
            Integer height,
            String url,
            Integer isShown
    ) {
        if (order != null) {
            this.order = order;
        }
        if (width != null) {
            this.width = WidgetSize.getByValue(width);
        }
        if (height != null) {
            this.height = WidgetSize.getByValue(height);
        }
        if (url != null) {
            this.url = UrlPath.builder().url(url).build();
        }
        if (isShown != null) {
            this.isShown = TrueOrFalse.getByValue(isShown);
        }
        return this;
    }

}

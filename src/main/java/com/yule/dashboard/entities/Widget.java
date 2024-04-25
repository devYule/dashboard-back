package com.yule.dashboard.entities;

import com.yule.dashboard.entities.enums.TrueOrFalse;
import com.yule.dashboard.entities.enums.WidgetSize;
import com.yule.dashboard.entities.enums.WidgetType;
import jakarta.persistence.*;
import lombok.*;
import org.openqa.selenium.remote.http.UrlPath;

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

    private String url;
    @Enumerated(EnumType.STRING)
    private TrueOrFalse isShown;
    @Enumerated(EnumType.STRING)
    private WidgetType type = WidgetType.BOOKMARK;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookmark_id")
    private BookMark bookmark;

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
            this.url = url;
        }
        if (isShown != null) {
            this.isShown = TrueOrFalse.getByValue(isShown);
        }
        return this;
    }

}

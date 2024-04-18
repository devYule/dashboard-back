package com.yule.dashboard.entities;

import com.yule.dashboard.entities.embeddable.UrlPath;
import com.yule.dashboard.entities.enums.TrueOrFalse;
import com.yule.dashboard.entities.enums.WidgetSize;
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

    @Column(name = "_order")
    private int order;
    private WidgetSize width;
    private WidgetSize height;
    @Embedded
    private UrlPath url;
    private TrueOrFalse isShown;

}

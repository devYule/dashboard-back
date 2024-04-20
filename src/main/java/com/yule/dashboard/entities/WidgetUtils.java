package com.yule.dashboard.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@DiscriminatorValue("wUtils")
public class WidgetUtils extends Widget {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "util_id")
    private Utils utils;
}

package com.yule.dashboard.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@DiscriminatorValue("wBookmark")
public class WidgetBookmark extends Widget {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookmark_id")
    private BookMark bookmarkId;
}

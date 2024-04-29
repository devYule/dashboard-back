package com.yule.dashboard.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@DiscriminatorValue("bookmark")
public class Bookmark extends Data {
    private String title;
    @Column(length = 2147483647)
    private String url;
    private String memo;
//    @OneToOne(cascade = CascadeType.PERSIST)
//    @JoinColumn(name = "bookmarkShotId")
//    private BookmarkScreenShot bookmarkShot;
}

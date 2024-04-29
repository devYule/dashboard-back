package com.yule.dashboard.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class BookmarkScreenShot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String shot;

    @OneToOne
    @JoinColumn(name = "bookmarkId")
    private Bookmark bookmark;


}

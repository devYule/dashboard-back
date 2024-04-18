package com.yule.dashboard.entities;

import com.yule.dashboard.entities.enums.HistoryType;
import com.yule.dashboard.entities.superclasses.CreatedAt;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class History extends CreatedAt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users userId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prev_id")
    private History prevId;

    @Enumerated
    private HistoryType type;

    @Column(name = "_value")
    private String value;

}

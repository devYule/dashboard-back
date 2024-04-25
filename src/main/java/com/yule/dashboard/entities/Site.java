package com.yule.dashboard.entities;

import com.yule.dashboard.entities.enums.SiteType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@DiscriminatorValue("site")
public class Site extends Data {
    @Enumerated(EnumType.STRING)
    private SiteType site;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "rank_id")
    private UserSiteRank rank;
}

package com.yule.dashboard.entities;

import com.yule.dashboard.entities.enums.SiteType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
}

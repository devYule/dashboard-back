package com.yule.dashboard.entities;

import com.yule.dashboard.entities.enums.SiteType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@DiscriminatorValue("site")
public class Site extends Data{
    private SiteType site;
}

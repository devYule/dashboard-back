package com.yule.dashboard.search.drivers.model;

import com.yule.dashboard.entities.enums.SiteType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public  class SiteInfo {
    private String title;
    private String link;
    private final List<String> content = new ArrayList<>();
    private SiteCategories category;
    private String iconPath;

}

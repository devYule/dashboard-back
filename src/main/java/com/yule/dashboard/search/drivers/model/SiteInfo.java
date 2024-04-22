package com.yule.dashboard.search.drivers.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class SiteInfo {
    private final List<String> title = new ArrayList<>();
    private String link;
    private final List<String> content = new ArrayList<>();
    private SiteCategories category;
    private String iconPath;
}

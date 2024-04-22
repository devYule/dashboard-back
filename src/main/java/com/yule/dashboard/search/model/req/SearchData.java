package com.yule.dashboard.search.model.req;

import com.yule.dashboard.search.drivers.model.SiteCategories;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class SearchData{
    private List<String> title;
    private String link;
    private List<String> content;
    private SiteCategories category;
    private String iconPath;
    private String subTitle;
}

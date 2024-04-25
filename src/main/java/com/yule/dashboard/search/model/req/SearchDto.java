package com.yule.dashboard.search.model.req;

import com.yule.dashboard.search.drivers.model.SiteCategories;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchDto {
    private String title;
    private String link;
    private List<String> content;
    private SiteCategories category;
    private String iconPath;
    private String subTitle;
    private long bookmarkId;
}

package com.yule.dashboard.search.model.resp;

import com.yule.dashboard.search.model.req.SearchDto;

import java.util.List;

public record SiteRespData(
        int siteId,
        List<SearchDto> contents
) {
}

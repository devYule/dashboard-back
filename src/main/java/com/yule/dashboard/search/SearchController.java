package com.yule.dashboard.search;

import com.yule.dashboard.pbl.BaseResponse;
import com.yule.dashboard.search.model.SearchType;
import com.yule.dashboard.search.model.resp.SiteRespData;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {
    private final SearchService searchService;


    @GetMapping
    public List<SiteRespData> search(@RequestParam String query) {
        return searchService.search(query);
    }

    @PatchMapping
    public BaseResponse changeSearchType(
            @NotNull
            @RequestParam Integer type) {
        return searchService.changeSearchType(SearchType.getByValue(type));
    }
    @GetMapping("/type")
    public BaseResponse getSearchType () {
        return searchService.getSearchType();
    }

}

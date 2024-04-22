package com.yule.dashboard.search;

import com.yule.dashboard.search.model.req.SearchData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {
    private final SearchService searchService;

    @GetMapping
    public List<List<SearchData>> search(@RequestParam String query) {
        return searchService.search(query);
    }

}

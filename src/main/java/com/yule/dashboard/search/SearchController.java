package com.yule.dashboard.search;

import com.yule.dashboard.search.model.req.SearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {
    private final SearchService searchService;


    @GetMapping
    public List<List<SearchDto>> search(@RequestParam String query) {
        return searchService.search(query);
    }

}

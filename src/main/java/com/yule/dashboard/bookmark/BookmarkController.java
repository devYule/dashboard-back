package com.yule.dashboard.bookmark;

import com.yule.dashboard.bookmark.model.data.req.BookmarkAddData;
import com.yule.dashboard.bookmark.model.data.resp.BookmarkData;
import com.yule.dashboard.bookmark.model.data.resp.BookmarkDataPage;
import com.yule.dashboard.bookmark.model.data.resp.BookmarkRemoveData;
import com.yule.dashboard.pbl.BaseResponse;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bm")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @GetMapping
    public List<BookmarkData> getBookmarks() {
        return bookmarkService.getBookmarks();
    }

    @GetMapping("/{page}")
    public BookmarkDataPage getBookmarksPage(@PathVariable int page) {
        return bookmarkService.getBookmarksPage(page);
    }

    @PostMapping
    public BaseResponse addBookmark(@RequestBody BookmarkAddData data) {
        return bookmarkService.addBookmark(data);
    }

    @DeleteMapping
    public BookmarkRemoveData removeBookmark(@NotNull @Min(1) @RequestParam Long id) {
        return bookmarkService.removeBookmark(id);
    }


}

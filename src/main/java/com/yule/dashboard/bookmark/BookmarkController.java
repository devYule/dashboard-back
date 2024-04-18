package com.yule.dashboard.bookmark;

import com.yule.dashboard.bookmark.model.data.req.BookmarkAddData;
import com.yule.dashboard.bookmark.model.data.resp.BookmarkData;
import com.yule.dashboard.pbl.BaseResponse;
import com.yule.dashboard.pbl.PrimaryKeyData;
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

    @PostMapping
    public BaseResponse addBookmark(@RequestBody BookmarkAddData data) {
        return bookmarkService.addBookmark(data);
    }

    @DeleteMapping
    public BaseResponse removeBookmark(@NotNull @Min(1) @RequestParam Long id) {
        return bookmarkService.removeBookmark(id);
    }


}

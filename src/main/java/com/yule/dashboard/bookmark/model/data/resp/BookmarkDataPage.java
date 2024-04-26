package com.yule.dashboard.bookmark.model.data.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class BookmarkDataPage {
    @Setter
    private int hasNext;
    private List<BookmarkData> bookmarks;
}

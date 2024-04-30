package com.yule.dashboard.bookmark.model.data.resp;

import java.util.List;

public record BookmarkRemoveData(
        Long value,
        List<Long> delWid
) {
}

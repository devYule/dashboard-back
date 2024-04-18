package com.yule.dashboard.bookmark.model.data.req;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record BookmarkAddData(
        @NotBlank
        @Length(max = 10)
        String title,
        @NotBlank
        String url,
        @Length(max = 50)
        String memo
) {
}

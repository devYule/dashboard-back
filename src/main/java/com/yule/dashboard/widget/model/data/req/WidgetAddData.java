package com.yule.dashboard.widget.model.data.req;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.checkerframework.checker.units.qual.min;
import org.hibernate.validator.constraints.Range;

public record WidgetAddData(
        @Min(1)
        int order,
        @Min(1)
        @Max(2)
        int width,
        @Min(1)
        @Max(2)
        int height,
        @NotBlank
        String url,
        @Min(0)
        @Max(1)
        int isShown,
        @Range(min = 0, max = 1)
        int type,
        @Min(1)
        Long bookmarkId
) {
}

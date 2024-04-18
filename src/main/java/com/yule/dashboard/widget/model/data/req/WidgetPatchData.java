package com.yule.dashboard.widget.model.data.req;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record WidgetPatchData(
        @NotNull
        @Min(1)
        Long id,
        @Min(1)
        Integer order,
        @Min(1)
        @Max(2)
        Integer width,
        @Min(1)
        @Max(2)
        Integer height,
        String url,
        @Min(0)
        @Max(1)
        Integer isShown
) {
}

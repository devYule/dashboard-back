package com.yule.dashboard.widget.model.data.resp;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record WidgetData(
        Long id,
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
        int isShown

) {
}

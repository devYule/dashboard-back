package com.yule.dashboard.widget.model.data.resp;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record WidgetData(
        Long id,
        @Min(1)
        @Max(2)
        int width,
        @Min(1)
        @Max(2)
        int height,
        @NotBlank
        String url,
        @Min(0)
        @NotBlank
        @Length(max = 10)
        String title,
        @Length(max = 50)
        String memo,
        String shot
) {
}

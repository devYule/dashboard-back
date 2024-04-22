package com.yule.dashboard.mypage.model.req;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record CheckMailData(
        @NotBlank
        String key,
        @NotBlank
        @Length(min = 6, max = 6)
        String code
) {
}

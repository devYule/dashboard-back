package com.yule.dashboard.user.model.data.req;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record SignupMailCheckData(
        @NotBlank
        String key,
        @NotBlank
        @Length(min = 6, max = 6)
        String code
) {
}

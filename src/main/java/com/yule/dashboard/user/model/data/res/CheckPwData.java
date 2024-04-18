package com.yule.dashboard.user.model.data.res;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record CheckPwData(
        @NotBlank
        String key,
        @NotBlank
        @Length(min = 6, max = 20)
        String pw
) {
}

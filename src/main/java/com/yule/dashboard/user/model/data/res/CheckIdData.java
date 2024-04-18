package com.yule.dashboard.user.model.data.res;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record CheckIdData(
        @NotBlank
        @Length(min = 4, max = 20)
        String loginId
) {
}

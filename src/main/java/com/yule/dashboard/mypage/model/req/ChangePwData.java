package com.yule.dashboard.mypage.model.req;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record ChangePwData(
        @NotBlank
        @Length(min = 6, max = 20)
        String pw
) {
}

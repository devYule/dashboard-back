package com.yule.dashboard.user.model.data.req;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record SignupInfoData(
        @NotBlank
        @Length(min = 4, max = 20)
        String loginId,
        @NotBlank
        @Length(min = 4, max = 15)
        String nick,
        @NotBlank
        @Length(min = 6, max = 20)
        String pw
) {
}

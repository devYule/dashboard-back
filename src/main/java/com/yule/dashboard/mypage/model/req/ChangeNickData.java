package com.yule.dashboard.mypage.model.req;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record ChangeNickData(
        @NotBlank
        @Length(min = 4, max = 15)
        String nick
) {
}

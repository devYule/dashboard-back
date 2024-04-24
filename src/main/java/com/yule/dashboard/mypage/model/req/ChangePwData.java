package com.yule.dashboard.mypage.model.req;

import com.yule.dashboard.pbl.exception.ExceptionMessages;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record ChangePwData(
        @NotBlank(message = ExceptionMessages.REQUEST_VALUE_MUST_BE_NOT_BLANK)
        @Length(min = 6, max = 20, message = ExceptionMessages.REQUEST_VALUE_LENGTH_MUST_BE_BETWEEN + "from 6 to 20")
        String prevPw,
        @NotBlank(message = ExceptionMessages.REQUEST_VALUE_MUST_BE_NOT_BLANK)
        @Length(min = 6, max = 20, message = ExceptionMessages.REQUEST_VALUE_LENGTH_MUST_BE_BETWEEN + "from 6 to 20")
        String pw
) {
}

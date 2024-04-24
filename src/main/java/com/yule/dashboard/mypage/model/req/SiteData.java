package com.yule.dashboard.mypage.model.req;

import com.yule.dashboard.pbl.exception.ExceptionMessages;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record SiteData(
        @Min(value = 0, message = ExceptionMessages.REQUEST_VALUE_RANGE_ERROR + "from 0 to 8")
        @Max(value = 8, message = ExceptionMessages.REQUEST_VALUE_RANGE_ERROR + "from 0 to 8")
        int site
) {
}

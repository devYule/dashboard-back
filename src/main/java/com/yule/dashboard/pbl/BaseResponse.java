package com.yule.dashboard.pbl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@AllArgsConstructor
@Builder
public class BaseResponse {

    private Long value;

    public BaseResponse() {
        this.value = 1L;
    }
}

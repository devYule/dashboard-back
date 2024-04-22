package com.yule.dashboard.mypage.model.req;

import jakarta.validation.constraints.Email;

public record ChangeMailData(
        @Email
        String mail
) {
}

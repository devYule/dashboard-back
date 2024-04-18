package com.yule.dashboard.user.model.data.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignupMailInfoData(
        @NotBlank
        String key,
        @NotBlank
        @Email
        String mail
) {

}

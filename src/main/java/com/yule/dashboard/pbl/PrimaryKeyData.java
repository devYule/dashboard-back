package com.yule.dashboard.pbl;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PrimaryKeyData(
        @NotNull
        @Min(1)
        Long id
) {
}

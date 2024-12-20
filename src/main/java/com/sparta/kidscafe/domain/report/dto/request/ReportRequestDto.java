package com.sparta.kidscafe.domain.report.dto.request;

import jakarta.validation.constraints.NotNull;

public record ReportRequestDto(
    @NotNull
    String content
) {
}

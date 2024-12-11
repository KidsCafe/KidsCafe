package com.sparta.kidscafe.domain.report.dto.response;

import com.sparta.kidscafe.common.enums.ReportType;

public record ReportResponseDto(
    Long id,
    Long userId,
    Long reviewId,
    String content,
    ReportType reportType
) {

}

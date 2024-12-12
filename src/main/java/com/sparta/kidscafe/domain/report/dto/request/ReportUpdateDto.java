package com.sparta.kidscafe.domain.report.dto.request;

import com.sparta.kidscafe.common.enums.ReportType;

public record ReportUpdateDto(
    ReportType status
){
}

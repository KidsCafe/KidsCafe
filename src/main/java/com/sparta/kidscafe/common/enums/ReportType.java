package com.sparta.kidscafe.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ReportType {
  PENDING("신고신청"),
  IN_PROGRESS("신고처리중"),
  COMPLETED("신고완료"),
  REJECTED("신고기각"),
  CANCELLED("신고취소");

  private final String name;

  @JsonCreator
  public static ReportType getReportType(String name) {
    for (ReportType reportType : ReportType.values()) {
      if (reportType.getName().equals(name)) {
        return reportType;
      }
    }
    return PENDING;
  }

  @JsonValue
  public String getName() {
    return name;
  }
}

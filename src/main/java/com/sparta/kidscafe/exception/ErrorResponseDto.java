package com.sparta.kidscafe.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
public class ErrorResponseDto {
  private String date;
  private int state;
  private String message;
  private String url;

  // BusinessException용 생성자
  public ErrorResponseDto(BusinessException ex, String requestUrl) {
    date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    state = ex.getErrorCode().getHttpStatus().value();
    message = ex.getMessage();
    url = requestUrl;
  }

  // ErrorCode만 사용하는 경우
  public ErrorResponseDto(ErrorCode errorCode, String requestUrl) {
    date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    state = errorCode.getHttpStatus().value();
    message = errorCode.getMessage();
    url = requestUrl;
  }

  // ErrorCode와 세부 메시지를 함께 사용하는 경우
  public ErrorResponseDto(ErrorCode errorCode, String requestUrl, String detailMsg) {
    date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    state = errorCode.getHttpStatus().value();
    // detailMsg가 있을 경우 detailMsg를 그대로 사용, 없으면 ErrorCode의 기본 메시지 사용
    message = detailMsg != null ? detailMsg : errorCode.getMessage();
    url = requestUrl;
  }
}

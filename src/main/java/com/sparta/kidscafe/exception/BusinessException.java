package com.sparta.kidscafe.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
  private final ErrorCode errorCode;

  public BusinessException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

  public BusinessException(ErrorCode errorCode, String detailMsg) {
    super(errorCode.getMessage() + " [" + detailMsg + "]");
    this.errorCode = errorCode;
  }
}

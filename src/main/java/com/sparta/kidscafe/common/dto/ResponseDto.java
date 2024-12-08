package com.sparta.kidscafe.common.dto;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

/**
 * 단일 객체를 반환할때 사용하는 responseDto
 */
@Getter
@SuperBuilder(builderMethodName = "createResponseBuilder")
public class ResponseDto<T> extends StatusDto{
  private T data;

  public static <T> ResponseDto<T> success(T data, HttpStatus status, String message) {
    return ResponseDto.<T>createResponseBuilder()
        .data(data)
        .message(message)
        .status(status.value())
        .build();
  }
}

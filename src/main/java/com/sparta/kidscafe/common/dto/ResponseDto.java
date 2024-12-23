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

  public static <T> ResponseDto<T> create(T data) {
    return ResponseDto.<T>createResponseBuilder()
        .data(data)
        .message(createMessage(data))
        .status(createStatus(data).value())
        .build();
  }

  private static <T> String createMessage(T data) {
    if(data == null)
      return "조회 결과가 없습니다.";
    else
      return "조회 성공";
  }

  private static <T> HttpStatus createStatus(T data) {
    if(data == null)
      return HttpStatus.NOT_FOUND;
    else
      return HttpStatus.OK;
  }
}

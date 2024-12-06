package com.sparta.kidscafe.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

// 단일 객체를 반환할때 사용하는 responseDto
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto<T> {
  private T data;

  public static <T> ResponseDto<T> success(T data) {
    return ResponseDto.<T>builder()
        .data(data)
        .build();
  }
}

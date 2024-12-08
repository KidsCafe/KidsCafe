package com.sparta.kidscafe.common.dto;

import java.util.List;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

/**
 * 리스트 형식을 반환할 때 사용하는 responseDto
 */
@Getter
@SuperBuilder(builderMethodName = "createResponseBuilder")
public class ListResponseDto<T> extends StatusDto {
  private List<T> data;

  public static <T> ListResponseDto<T> success(List<T> data, HttpStatus status, String message) {
    return ListResponseDto.<T>createResponseBuilder()
        .data(data)
        .status(status.value())
        .message(message)
        .build();
  }
}

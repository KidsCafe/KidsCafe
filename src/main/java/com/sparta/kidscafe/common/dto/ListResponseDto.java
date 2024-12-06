package com.sparta.kidscafe.common.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

// 리스트 형식을 반환할 때 사용하는 responseDto
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListResponseDto<T> {

  private List<T> data;

  public static <T> ListResponseDto<T> success(List<T> data) {
    return ListResponseDto.<T>builder()
        .data(data)
        .build();
  }
}

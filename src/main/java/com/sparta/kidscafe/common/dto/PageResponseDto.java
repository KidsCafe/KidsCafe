package com.sparta.kidscafe.common.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

// 페아징 객체를 반환할때 사용하는 responseDto
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponseDto<T> {
  private List<T> data;
  private int page;
  private int size;
  private int totalPage;

  public static <T> PageResponseDto<T> success(List<T> data, Pageable pageable, int totalPage) {
    return PageResponseDto.<T>builder()
        .data(data)
        .page(pageable.getPageNumber() + 1)
        .size(pageable.getPageSize())
        .totalPage(totalPage)
        .build();
  }
}

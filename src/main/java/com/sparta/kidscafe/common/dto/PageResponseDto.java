package com.sparta.kidscafe.common.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponseDto<T> {
  private List<T> data;
  private int page;
  private int size;
  private int totalPage;

  public static <T> PageResponseDto<T> of(List<T> data, Pageable pageable, int totalPage) {
    return PageResponseDto.<T>builder()
        .data(data)
        .page(pageable.getPageNumber() + 1)
        .size(pageable.getPageSize())
        .totalPage(totalPage)
        .build();
  }
}

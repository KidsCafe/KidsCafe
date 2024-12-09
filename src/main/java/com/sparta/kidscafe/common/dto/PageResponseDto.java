package com.sparta.kidscafe.common.dto;

import java.util.List;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

// 페이징 객체를 반환할때 사용하는 responseDto
@Getter
@SuperBuilder(builderMethodName = "createResponseBuilder")
public class PageResponseDto<T> extends StatusDto {
  private List<T> data;
  private int page;
  private int size;
  private int totalPage;

  public static <T> PageResponseDto<T> success(Page<T> data, HttpStatus status, String message) {
    Pageable pageable = data.getPageable();
    return PageResponseDto.<T>createResponseBuilder()
        .data(data.stream().toList())
        .status(status.value())
        .message(message)
        .page(pageable.getPageNumber() + 1)
        .size(pageable.getPageSize())
        .totalPage(data.getTotalPages())
        .build();
  }
}

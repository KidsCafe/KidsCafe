package com.sparta.kidscafe.domain.bookmark.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkCreateRequestDto {

  private Long userId;
  private Long cafeId;
}

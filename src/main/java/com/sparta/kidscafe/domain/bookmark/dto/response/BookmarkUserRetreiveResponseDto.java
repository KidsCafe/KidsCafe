package com.sparta.kidscafe.domain.bookmark.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookmarkUserRetreiveResponseDto {

  private Long cafeId;
  private String cafeName;

  public BookmarkUserRetreiveResponseDto(Long cafeId, String cafeName) {
    this.cafeId = cafeId;
    this.cafeName = cafeName;
  }


}

package com.sparta.kidscafe.domain.bookmark.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookmarkOwnerRetreiveResponseDto {

  private Long userId;
  private String userName;

  public BookmarkOwnerRetreiveResponseDto(Long userId, String userName) {
    this.userId = userId;
    this.userName = userName;
  }
}

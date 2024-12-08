package com.sparta.kidscafe.domain.bookmark.dto.response;

import com.sparta.kidscafe.domain.bookmark.entity.Bookmark;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkResponseDto {

  private Long id;
  private Long userId;
  private Long cafeId;

  public static BookmarkResponseDto from(Bookmark bookmark) {
    return new BookmarkResponseDto(
        bookmark.getId(),
        bookmark.getUser().getId(),
        bookmark.getCafe().getId()
    );
  }
}

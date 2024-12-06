package com.sparta.kidscafe.domain.bookmark.controller;

import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.domain.bookmark.service.BookmarkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BookmarkController {

  private final BookmarkService bookmarkService;

  @PostMapping("/cafes/{cafeId}/bookmarks")
  public ResponseEntity<StatusDto> toggleBookmark(
      @Valid @PathVariable(value = "cafeId") Long cafeId) {

    boolean isBookmarked = bookmarkService.toggleBookmark(1L, cafeId);

    if (isBookmarked) {
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(StatusDto.builder().status(HttpStatus.CREATED.value()).message("즐겨찾기 추가").build());
    } else {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
  }

}

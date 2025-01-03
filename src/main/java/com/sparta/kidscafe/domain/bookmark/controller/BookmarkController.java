package com.sparta.kidscafe.domain.bookmark.controller;

import com.sparta.kidscafe.common.annotation.Auth;
import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.PageResponseDto;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.domain.bookmark.dto.response.BookmarkOwnerRetreiveResponseDto;
import com.sparta.kidscafe.domain.bookmark.dto.response.BookmarkUserRetreiveResponseDto;
import com.sparta.kidscafe.domain.bookmark.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BookmarkController {

  private final BookmarkService bookmarkService;

  // 즐겨찾기 추가 & 삭제(토글식)
  @PostMapping("/cafes/{cafeId}/bookmarks")
  public ResponseEntity<StatusDto> toggleBookmark(
      @Auth AuthUser authUser,
      @PathVariable(value = "cafeId") Long cafeId) {

    boolean isBookmarked = bookmarkService.toggleBookmark(authUser.getId(), cafeId);

    if (isBookmarked) {
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(StatusDto.builder().status(HttpStatus.CREATED.value()).message("즐겨찾기 추가").build());
    } else {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
  }

  // 즐겨찾기 조회(User용)
  @GetMapping("/users/bookmarks")
  public ResponseEntity<PageResponseDto<BookmarkUserRetreiveResponseDto>> getBookmarksByUser(
      @Auth AuthUser authUser,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    PageResponseDto<BookmarkUserRetreiveResponseDto> response = bookmarkService.getBookmarkByUser(
        authUser, page, size);
    return ResponseEntity.ok(response);
  }

  // 즐겨찾기 조회(Owner용)
  @GetMapping("/owners/bookmarks/{cafeId}")
  public ResponseEntity<PageResponseDto<BookmarkOwnerRetreiveResponseDto>> getBookmarksByOwner(
      @Auth AuthUser authUser,
      @PathVariable Long cafeId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    PageResponseDto<BookmarkOwnerRetreiveResponseDto> response = bookmarkService.getBookmarkByOwner(
        authUser, cafeId, page, size);
    return ResponseEntity.ok(response);
  }

}

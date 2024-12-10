package com.sparta.kidscafe.domain.bookmark.service;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import com.sparta.kidscafe.common.dto.PageResponseDto;
import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.domain.bookmark.dto.response.BookmarkUserRetreiveResponseDto;
import com.sparta.kidscafe.domain.bookmark.entity.Bookmark;
import com.sparta.kidscafe.domain.bookmark.repository.BookmarkRepository;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.domain.user.entity.User;
import com.sparta.kidscafe.domain.user.repository.UserRepository;
import com.sparta.kidscafe.exception.BusinessException;
import com.sparta.kidscafe.exception.ErrorCode;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;


@ExtendWith(MockitoExtension.class)
public class BookmarkServiceTest {

  @InjectMocks
  private BookmarkService bookmarkService;

  @Mock
  private BookmarkRepository bookmarkRepository;

  @Mock
  private CafeRepository cafeRepository;

  @Mock
  private UserRepository userRepository;

  @Test
  @DisplayName("즐겨찾기 추가: 성공")
  void addToggleBookmark_success() {
    // Given
    Long userId = 1L;
    Long cafeId = 1L;

    User user = new User();
    Cafe cafe = new Cafe();

    Mockito.when(userRepository.findById(userId))
        .thenReturn(Optional.of(user));
    Mockito.when(cafeRepository.findById(cafeId))
        .thenReturn(Optional.of(cafe));
    Mockito.when(bookmarkRepository.findByUserAndCafe(user, cafe))
        .thenReturn(Optional.empty());

    // When
    boolean result = bookmarkService.toggleBookmark(userId, cafeId);

    // Then
    Assertions.assertTrue(result);
    Mockito.verify(bookmarkRepository, Mockito.times(1))
        .save(Mockito.any(Bookmark.class));
    Mockito.verify(bookmarkRepository, Mockito.never())
        .delete(Mockito.any(Bookmark.class));
  }

  @Test
  @DisplayName("즐겨찾기 추가: 실패 = 삭제")
  void addToggleBookmark_fail() {
    // Given
    Long userId = 1L;
    Long cafeId = 1L;

    User user = new User();
    Cafe cafe = new Cafe();
    Bookmark bookmark = new Bookmark();

    Mockito.when(userRepository.findById(userId))
        .thenReturn(Optional.of(user));
    Mockito.when(cafeRepository.findById(cafeId))
        .thenReturn(Optional.of(cafe));
    Mockito.when(bookmarkRepository.findByUserAndCafe(user, cafe))
        .thenReturn(Optional.of(bookmark));

    // When
    boolean result = bookmarkService.toggleBookmark(userId, cafeId);

    // Then
    Assertions.assertFalse(result);
    Mockito.verify(bookmarkRepository, Mockito.times(1))
        .delete(bookmark);
    Mockito.verify(bookmarkRepository, Mockito.never())
        .save(Mockito.any(Bookmark.class));
  }

  @Test
  @DisplayName("사용자 전용 즐겨찾기 조회: 성공")
  void getBookmark_success() {
    // Given
    Long userId = 1L;
    int page = 0;
    int size = 10;

    User user = User.builder()
        .id(userId)
        .role(RoleType.USER)
        .build();

    List<Bookmark> bookmarks = List.of(
        Bookmark.builder()
            .cafe(Cafe.builder().id(101L).name("Cafe A").build())
            .build(),
        Bookmark.builder()
            .cafe(Cafe.builder().id(102L).name("Cafe B").build())
            .build()
    );
    Page<Bookmark> mockPage = new PageImpl<>(bookmarks, PageRequest.of(page, size),
        bookmarks.size());

    Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    Mockito.when(bookmarkRepository.findAllByUserId(userId, PageRequest.of(page, size, Sort.by(
        Direction.DESC, "createdAt")))).thenReturn(mockPage);

    // When
    PageResponseDto<BookmarkUserRetreiveResponseDto> response = bookmarkService.getBookmark(userId,
        page, size);

    // Then
    assertNotNull(response);
    assertEquals(2, response.getData().size());
    assertEquals("즐겨찾기 조회(사용자용) 성공", response.getMessage());
    assertEquals(1, response.getPage());
    assertEquals(10, response.getSize());
    assertEquals(1, response.getTotalPage());

    BookmarkUserRetreiveResponseDto responseDto = response.getData().get(0);
    assertEquals(101L, responseDto.getCafeId());
    assertEquals("Cafe A", responseDto.getCafeName());
  }

  @Test
  @DisplayName("인증되지 않은 사용자가 즐겨찾기 조회를 시도한 경우")
  void getBookmark_UserNotFound() {
    // Given
    Long userId = 1L;
    given(userRepository.findById(userId)).willReturn(Optional.empty());

    // When & Then
    BusinessException ex = assertThrows(BusinessException.class, () ->
        bookmarkService.getBookmark(userId, 0, 10)
    );
    assertEquals(ErrorCode.USER_NOT_FOUND, ex.getErrorCode());
  }

  @Test
  @DisplayName("사용자(User) 권한이 없는 경우")
  void getBookmark_Unauthorized() {
    // Given
    Long userId = 1L;
    User user = new User(userId, "test@example.com", RoleType.ADMIN); // 비권한 사용자
    given(userRepository.findById(userId)).willReturn(Optional.of(user));

    // When & Then
    BusinessException ex = assertThrows(BusinessException.class, () ->
        bookmarkService.getBookmark(userId, 0, 10)
    );
    assertEquals(ErrorCode.UNAUTHORIZED, ex.getErrorCode());
  }

  @Test
  @DisplayName("즐겨찾기 목록이 비어있는 경우")
  void getBookmark_NoBookmarks() {
    // Given
    Long userId = 1L;
    User user = new User(userId, "test@example.com", RoleType.USER);
    Page<Bookmark> emptyPage = Page.empty();
    given(userRepository.findById(userId)).willReturn(Optional.of(user));
    given(bookmarkRepository.findAllByUserId(eq(userId), any(Pageable.class))).willReturn(
        emptyPage);

    // When & Then
    BusinessException ex = assertThrows(BusinessException.class, () ->
        bookmarkService.getBookmark(userId, 0, 10)
    );
    assertEquals(ErrorCode.NO_BOOKMARKS_FOUND, ex.getErrorCode());
  }
}

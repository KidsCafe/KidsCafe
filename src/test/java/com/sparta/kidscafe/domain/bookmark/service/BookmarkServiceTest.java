package com.sparta.kidscafe.domain.bookmark.service;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.PageResponseDto;
import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.domain.bookmark.dto.response.BookmarkOwnerRetreiveResponseDto;
import com.sparta.kidscafe.domain.bookmark.dto.response.BookmarkUserRetreiveResponseDto;
import com.sparta.kidscafe.domain.bookmark.entity.Bookmark;
import com.sparta.kidscafe.domain.bookmark.repository.BookmarkRepository;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.domain.user.entity.User;
import com.sparta.kidscafe.domain.user.repository.UserRepository;
import com.sparta.kidscafe.exception.BusinessException;
import com.sparta.kidscafe.exception.ErrorCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.Direction;

import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


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

  private AuthUser authUser;
  private Pageable pageable;

  @BeforeEach
  void setUp() {
    authUser = new AuthUser(1L, "Test@example.com", RoleType.USER);
    pageable = PageRequest.of(0, 10, Sort.by(Direction.DESC, "createdAt"));
  }

  @Test
  @DisplayName("즐겨찾기 추가: 성공")
  void addToggleBookmark_success() {
    // Given
    Long userId = 1L;
    Long cafeId = 1L;

    User user = new User(userId, "Test@Example.com", RoleType.USER);
    Cafe cafe = Cafe.builder()
        .id(cafeId)
        .name("Test Cafe")
        .build();

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(cafeRepository.findById(cafeId)).thenReturn(Optional.of(cafe));
    when(bookmarkRepository.findByUserAndCafe(user, cafe)).thenReturn(Optional.empty());

    // When
    boolean result = bookmarkService.toggleBookmark(userId, cafeId);

    // Then
    Assertions.assertTrue(result);
    verify(bookmarkRepository, Mockito.times(1)).save(any(Bookmark.class));
    verify(bookmarkRepository, Mockito.never()).delete(any(Bookmark.class));
  }

  @Test
  @DisplayName("즐겨찾기 추가: 실패 = 삭제")
  void addToggleBookmark_fail() {
    // Given
    Long userId = 1L;
    Long cafeId = 1L;

    User user = new User(userId, "Test@Example.com", RoleType.USER);
    Cafe cafe = Cafe.builder()
        .id(cafeId)
        .name("Test Cafe")
        .build();
    Bookmark bookmark = new Bookmark(1L, user, cafe);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(cafeRepository.findById(cafeId)).thenReturn(Optional.of(cafe));
    when(bookmarkRepository.findByUserAndCafe(user, cafe)).thenReturn(Optional.of(bookmark));

    // When
    boolean result = bookmarkService.toggleBookmark(userId, cafeId);

    // Then
    assertFalse(result);
    verify(bookmarkRepository, Mockito.times(1)).delete(bookmark);
    verify(bookmarkRepository, Mockito.never()).save(any(Bookmark.class));
  }

  @Test
  @DisplayName("사용자 전용 즐겨찾기 조회: 성공")
  void getBookmarkByUser_success() {
    // Given
    int page = 0;
    int size = 10;

    List<Bookmark> bookmarks = List.of(
        Bookmark.builder()
            .cafe(Cafe.builder().id(101L).name("Cafe A").build())
            .build(),
        Bookmark.builder()
            .cafe(Cafe.builder().id(102L).name("Cafe B").build())
            .build()
    );
    Page<Bookmark> mockPage = new PageImpl<>(bookmarks, pageable, bookmarks.size());

    when(bookmarkRepository.findAllByUserId(eq(authUser.getId()), any(Pageable.class))).thenReturn(mockPage);

    // When
    PageResponseDto<BookmarkUserRetreiveResponseDto> response = bookmarkService.getBookmarkByUser(
        authUser,
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
  @DisplayName("사용자 전용 즐겨찾기 조회: 실패 - 권한 없음")
  void getBookmarkByUser_Unauthorized() {
    // Given
    AuthUser unauthorizedUser = new AuthUser(2L, "Test@example.com", RoleType.ADMIN);// 비권한 사용자

    // When & Then
    BusinessException ex = assertThrows(BusinessException.class, () ->
        bookmarkService.getBookmarkByUser(unauthorizedUser, 0, 10)
    );
    assertEquals(ErrorCode.UNAUTHORIZED, ex.getErrorCode());
  }

  @Test
  @DisplayName("사용자 전용 즐겨찾기 조회: 실패 - 즐겨찾기 목록이 비어있는 경우")
  void getBookmarkByUser_NoBookmarks() {
    // Given
    Page<Bookmark> emptyPage = Page.empty();
    when(bookmarkRepository.findAllByUserId(eq(authUser.getId()), any(Pageable.class))).thenReturn(emptyPage);
    // When & Then
    BusinessException ex = assertThrows(BusinessException.class, () ->
        bookmarkService.getBookmarkByUser(authUser, 0, 10)
    );
    assertEquals(ErrorCode.NO_BOOKMARKS_FOUND, ex.getErrorCode());
  }

  @Test
  @DisplayName("카페 전용 즐겨찾기 조회: 성공")
  void getBookmarkByOwner_success() {
    // Given
    AuthUser ownerUser = new AuthUser(101L, "Test@example.com", RoleType.OWNER);
    Long cafeId = 1L;
    int page = 0;
    int size = 10;

    List<Bookmark> bookmarkList = List.of(
        Bookmark.builder()
            .user(User.builder().id(10L).name("Tester 1").role(RoleType.USER).build())
            .cafe(Cafe.builder().id(1L).name("Cafe A").build())
            .build(),
        Bookmark.builder()
            .user(User.builder().id(20L).name("Tester 2").role(RoleType.USER).build())
            .cafe(Cafe.builder().id(1L).name("Cafe A").build())
            .build()
    );
    Page<Bookmark> bookmarkPage = new PageImpl<>(bookmarkList, pageable, bookmarkList.size());

    when(bookmarkRepository.findAllByCafeId(eq(cafeId), any(Pageable.class))).thenReturn(bookmarkPage);

    // When
    PageResponseDto<BookmarkOwnerRetreiveResponseDto> response =
        bookmarkService.getBookmarkByOwner(ownerUser, cafeId, page, size);

    // Then
    assertNotNull(response);
    assertEquals("즐겨찾기 조회(카페용) 성공", response.getMessage());
    assertEquals(1, response.getPage());
    assertEquals(10, response.getSize());
    assertEquals(1, response.getTotalPage());

    assertEquals(2, response.getData().size());

    BookmarkOwnerRetreiveResponseDto response1Dto = response.getData().get(0);
    assertEquals(10L, response1Dto.getUserId());
    assertEquals("Tester 1", response1Dto.getUserName());

    BookmarkOwnerRetreiveResponseDto response2Dto = response.getData().get(1);
    assertEquals(20L, response2Dto.getUserId());
    assertEquals("Tester 2", response2Dto.getUserName());
  }

  @Test
  @DisplayName("카페 전용 즐겨찾기 조회: 실패 - 권한 없음")
  void getBookmarkByOwner_unauthorized() {
    // Given
    Long cafeId = 1L;
    int page = 0;
    int size = 10;

    // When & Then
    BusinessException ex = assertThrows(
        BusinessException.class, () -> bookmarkService.getBookmarkByOwner(authUser, cafeId, page, size)
    );
    assertEquals(ErrorCode.UNAUTHORIZED, ex.getErrorCode());
  }
}



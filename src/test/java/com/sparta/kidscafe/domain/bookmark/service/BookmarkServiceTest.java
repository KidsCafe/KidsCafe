package com.sparta.kidscafe.domain.bookmark.service;

import com.sparta.kidscafe.domain.bookmark.entity.Bookmark;
import com.sparta.kidscafe.domain.bookmark.repository.BookmarkRepository;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.domain.user.entity.User;
import com.sparta.kidscafe.domain.user.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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
}

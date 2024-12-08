package com.sparta.kidscafe.domain.bookmark.service;

import com.sparta.kidscafe.domain.bookmark.entity.Bookmark;
import com.sparta.kidscafe.domain.bookmark.repository.BookmarkRepository;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.domain.user.entity.User;
import com.sparta.kidscafe.domain.user.repository.UserRepository;
import com.sparta.kidscafe.exception.BusinessException;
import com.sparta.kidscafe.exception.ErrorCode;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkService {

  private final BookmarkRepository bookmarkRepository;
  private final CafeRepository cafeRepository;
  private final UserRepository userRepository;


  // 즐겨찾기 추가 & 삭제 로직: @return true: 즐겨찾기에 추가, false: 즐겨찾기에서 삭제
  public boolean toggleBookmark(Long userId, Long cafeId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    Cafe cafe = cafeRepository.findById(cafeId)
        .orElseThrow(() -> new BusinessException(ErrorCode.CAFE_NOT_FOUND));

    Optional<Bookmark> bookmarkOptional = bookmarkRepository.findByUserAndCafe(user, cafe);

    if (bookmarkOptional.isPresent()) {
      // 즐겨찾기에 있는 경우 삭제
      bookmarkRepository.delete(bookmarkOptional.get());
      return false;
    } else {
      Bookmark bookmark = Bookmark.builder()
          .user(user)
          .cafe(cafe)
          .build();
      bookmarkRepository.save(bookmark);
      return true;
    }
  }
}

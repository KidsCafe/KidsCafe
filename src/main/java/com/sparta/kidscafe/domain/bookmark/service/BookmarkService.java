package com.sparta.kidscafe.domain.bookmark.service;

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
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
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

  // 즐겨찾기 조회(사용자용)
  public PageResponseDto<BookmarkUserRetreiveResponseDto> getBookmark(Long userId, int page,
      int size) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    if (!user.getRole().equals(RoleType.USER)) {
      throw new BusinessException(ErrorCode.UNAUTHORIZED);
    }
    Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.DESC, "createdAt"));
    Page<Bookmark> bookmarks = bookmarkRepository.findAllByUserId(userId, pageable);

    if (bookmarks.isEmpty()) {
      throw new BusinessException(ErrorCode.NO_BOOKMARKS_FOUND);
    }

    Page<BookmarkUserRetreiveResponseDto> bookmarkDtos = bookmarks.map(bookmark ->
        new BookmarkUserRetreiveResponseDto(
            bookmark.getCafe().getId(),
            bookmark.getCafe().getName()
        )
    );
    return PageResponseDto.success(bookmarkDtos, HttpStatus.OK, "즐겨찾기 조회(사용자용) 성공");
  }
}

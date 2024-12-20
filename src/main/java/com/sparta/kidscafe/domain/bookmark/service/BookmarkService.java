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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
  public PageResponseDto<BookmarkUserRetreiveResponseDto> getBookmarkByUser(AuthUser authUser,
                                                                            int page,
                                                                            int size) {
    if (!authUser.getRoleType().equals(RoleType.USER)) {
      throw new BusinessException(ErrorCode.UNAUTHORIZED);
    }
    Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Direction.DESC, "createdAt"));
    Page<Bookmark> bookmarksForUser = bookmarkRepository.findAllByUserId(authUser.getId(),
        pageable);

    if (bookmarksForUser.isEmpty()) {
      throw new BusinessException(ErrorCode.NO_BOOKMARKS_FOUND);
    }

    Page<BookmarkUserRetreiveResponseDto> bookmarkDtoForUser = bookmarksForUser.map(bookmark ->
        new BookmarkUserRetreiveResponseDto(
            bookmark.getCafe().getId(),
            bookmark.getCafe().getName()
        )
    );
    return PageResponseDto.success(bookmarkDtoForUser, HttpStatus.OK, "즐겨찾기 조회(사용자용) 성공");
  }

  // 즐겨찾기 조회(카페용)
  public PageResponseDto<BookmarkOwnerRetreiveResponseDto> getBookmarkByOwner(AuthUser authUser,
                                                                              Long cafeId, int page, int size) {
    if (!authUser.getRoleType().equals(RoleType.OWNER)) {
      throw new BusinessException(ErrorCode.UNAUTHORIZED);
    }
    Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Direction.DESC, "createdAt"));
    Page<Bookmark> bookmarksForCafe = bookmarkRepository.findAllByCafeId(cafeId, pageable);

    Page<BookmarkOwnerRetreiveResponseDto> bookmarkDtoForCafe = bookmarksForCafe.map(
        bookmark -> new BookmarkOwnerRetreiveResponseDto(
            bookmark.getUser().getId(),
            bookmark.getUser().getName()
        )
    );
    return PageResponseDto.success(bookmarkDtoForCafe, HttpStatus.OK, "즐겨찾기 조회(카페용) 성공");
  }
}

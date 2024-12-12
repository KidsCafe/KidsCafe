package com.sparta.kidscafe.domain.bookmark.repository;

import com.sparta.kidscafe.domain.bookmark.entity.Bookmark;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

  Optional<Bookmark> findByUserAndCafe(User user, Cafe cafe);

  Page<Bookmark> findAllByUserId(Long userId, Pageable pageable);

  Page<Bookmark> findAllByCafeId(Long cafeId, Pageable pageable);
}

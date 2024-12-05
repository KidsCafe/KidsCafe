package com.sparta.kidscafe.domain.bookmark.repository;

import com.sparta.kidscafe.domain.bookmark.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
}

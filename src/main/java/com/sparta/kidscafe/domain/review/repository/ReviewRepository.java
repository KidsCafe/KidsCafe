package com.sparta.kidscafe.domain.review.repository;

import com.sparta.kidscafe.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

  Page<Review> findByCafeId(Long cafeId, Pageable pageable);

  Page<Review> findAllByUserId(Long id, Pageable pageable);

  @Query("SELECT r FROM Review r LEFT JOIN FETCH r.replies WHERE r.cafe.id = :cafeId AND r.parentReview IS NULL")
  Page<Review> findByCafeIdAndParentReviewIsNullWithReplies(@Param("cafeId") Long cafeId, Pageable pageable);
}

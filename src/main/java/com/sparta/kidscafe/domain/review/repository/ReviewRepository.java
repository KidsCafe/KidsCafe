package com.sparta.kidscafe.domain.review.repository;

import com.sparta.kidscafe.domain.review.entity.Review;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

  Page<Review> findByCafeId(Long cafeId, Pageable pageable);

  Page<Review> findByReviewId(Long id);
}

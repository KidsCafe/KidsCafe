package com.sparta.kidscafe.domain.review.repository;

import com.sparta.kidscafe.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}

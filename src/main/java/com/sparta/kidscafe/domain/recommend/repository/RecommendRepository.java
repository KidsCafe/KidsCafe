package com.sparta.kidscafe.domain.recommend.repository;

import com.sparta.kidscafe.domain.recommend.entity.Recommend;
import com.sparta.kidscafe.domain.review.entity.Review;
import com.sparta.kidscafe.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendRepository extends JpaRepository<Recommend, Long> {

  Optional<Recommend> findByUserAndReview(User user, Review review);
}

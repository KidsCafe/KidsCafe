package com.sparta.kidscafe.domain.recommend.service;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.domain.recommend.entity.Recommend;
import com.sparta.kidscafe.domain.recommend.repository.RecommendRepository;
import com.sparta.kidscafe.domain.review.entity.Review;
import com.sparta.kidscafe.domain.review.repository.ReviewRepository;
import com.sparta.kidscafe.domain.user.entity.User;
import com.sparta.kidscafe.domain.user.repository.UserRepository;
import com.sparta.kidscafe.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.sparta.kidscafe.exception.ErrorCode.USER_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class RecommendService {

  private final UserRepository userRepository;
  private final ReviewRepository reviewRepository;
  private final RecommendRepository recommendRepository;

  public boolean createRecommend(Long reviewId, AuthUser authUser) {

    Long userId = authUser.getId();

    User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

    Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

    Optional<Recommend> optionalRecommend = recommendRepository.findByUserAndReview(user, review);

    if (optionalRecommend.isPresent()) {
      recommendRepository.delete(optionalRecommend.get());
      return false;
    } else {
      Recommend recommend = Recommend.builder()
          .user(user)
          .review(review)
          .build();
      recommendRepository.save(recommend);
      return true;
    }
  }
}

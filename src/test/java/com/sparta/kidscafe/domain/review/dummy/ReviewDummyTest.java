package com.sparta.kidscafe.domain.review.dummy;

import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.domain.reservation.entity.Reservation;
import com.sparta.kidscafe.domain.review.entity.Review;
import com.sparta.kidscafe.domain.review.entity.ReviewImage;
import com.sparta.kidscafe.domain.review.repository.ReviewImageRepository;
import com.sparta.kidscafe.domain.review.repository.ReviewRepository;
import com.sparta.kidscafe.domain.user.entity.User;
import com.sparta.kidscafe.domain.user.repository.UserRepository;
import com.sparta.kidscafe.dummy.DummyReview;
import com.sparta.kidscafe.dummy.DummyReviewImage;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@Tag("dummy-test")
@SpringBootTest
public class ReviewDummyTest {
  @Autowired
  private ReviewRepository reviewRepository;

  @Autowired
  private ReviewImageRepository reviewImageRepository;

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private CafeRepository cafeRepository;

  @Test
  @Transactional
  @Rollback(false)
  void createReview() {
    // user dummy tes 돌려야함
    // cafe dummy test 돌려야함
    List<User> users = userRepository.findAllByRole(RoleType.USER);
    List<Cafe> cafes = cafeRepository.findAll();
    Collections.shuffle(cafes);
    if(cafes.size() > 5)
      cafes = cafes.subList(0, 3);

    for(User user : users) {
      for(Cafe cafe : cafes) {
        Review review = DummyReview.createDummyReview(user, cafe);
        reviewRepository.save(review);
        List<ReviewImage> reviewImages = DummyReviewImage.createDummyCafeImages(review, 5);
        reviewImageRepository.saveAll(reviewImages);
      }
    }
  }
}

package com.sparta.kidscafe.domain.review.dummy;

import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.domain.reservation.entity.Reservation;
import com.sparta.kidscafe.domain.review.entity.Review;
import com.sparta.kidscafe.domain.review.entity.ReviewImage;
import com.sparta.kidscafe.domain.review.repository.ReviewImageRepository;
import com.sparta.kidscafe.domain.review.repository.ReviewRepository;
import com.sparta.kidscafe.domain.user.entity.User;
import com.sparta.kidscafe.domain.user.repository.UserRepository;
import com.sparta.kidscafe.dummy.DummyReview;
import com.sparta.kidscafe.dummy.DummyReviewImage;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class ReviewDummyTest {
  @Autowired
  private ReviewRepository reviewRepository;

  @Autowired
  private ReviewImageRepository reviewImageRepository;

  @Autowired
  private UserRepository userRepository;

  @Test
  @Transactional
  @Rollback(false)
  void createReview() {
    // user dummy tes 돌려야함
    // cafe dummy test 돌려야함
    // 예약 dummy test 돌리고 와야함
    List<User> users = userRepository.findAllByRole(RoleType.USER);
    for(User user : users) {
      List<Reservation> reservations =  user.getReservations();
      for(Reservation reservation : reservations) {
        Review review = DummyReview.createDummyReview(user, reservation);
        reviewRepository.save(review);

        List<ReviewImage> reviewImages = DummyReviewImage.createDummyCafeImages(review, 5);
        reviewImageRepository.saveAll(reviewImages);
      }
    }
  }
}

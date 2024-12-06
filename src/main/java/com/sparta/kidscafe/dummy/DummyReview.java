package com.sparta.kidscafe.dummy;

import com.sparta.kidscafe.common.util.TestUtil;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.reservation.entity.Reservation;
import com.sparta.kidscafe.domain.review.entity.Review;
import com.sparta.kidscafe.domain.user.entity.User;
import java.util.ArrayList;
import java.util.List;

public class DummyReview {
  public static Review createDummyReview(User user, Reservation reservation) {
    double randomStar = TestUtil.getRandomDouble(1, 5);
    String randomContent = TestUtil.getRandomString(20);
    return Review.builder()
        .user(user)
        .cafe(reservation.getCafe())
        .reservation(reservation)
        .star(randomStar)
        .content(randomContent)
        .build();
  }

  public static List<Review> createDummyReviews(User user, List<Reservation> Reservation) {
    List<Review> reviews = new ArrayList<>();
    for(Reservation reservation : Reservation) {
      reviews.add(createDummyReview(user, reservation));
    }
    return reviews;
  }
}

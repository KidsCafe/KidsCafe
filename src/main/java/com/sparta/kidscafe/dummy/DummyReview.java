package com.sparta.kidscafe.dummy;

import com.sparta.kidscafe.common.util.TestUtil;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.review.entity.Review;
import com.sparta.kidscafe.domain.user.entity.User;

public class DummyReview {

  public static Review createDummyReview(Long id, User user, Cafe cafe) {
    double randomStar = TestUtil.getRandomDouble(1, 5);
    String randomContent = TestUtil.getRandomString(20);
    return Review.builder()
        .id(id)
        .user(user)
        .cafe(cafe)
        .star(randomStar)
        .content(randomContent)
        .build();
  }

  public static Review createDummyReview(User user, Cafe cafe) {
    double randomStar = TestUtil.getRandomDouble(1, 5);
    String randomContent = TestUtil.getRandomString(20);
    return Review.builder()
        .user(user)
        .cafe(cafe)
        .star(randomStar)
        .content(randomContent)
        .build();
  }
}

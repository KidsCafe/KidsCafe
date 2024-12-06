package com.sparta.kidscafe.dummy;

import com.sparta.kidscafe.common.util.TestUtil;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.entity.CafeImage;
import com.sparta.kidscafe.domain.review.entity.Review;
import com.sparta.kidscafe.domain.review.entity.ReviewImage;
import java.util.ArrayList;
import java.util.List;

public class DummyReviewImage {
  public static ReviewImage createDummyReviewImage(Review review) {
    String randomImagePath = "http://..." + TestUtil.getRandomString(10) + ".jpg";
    return ReviewImage.builder()
        .review(review)
        .imagePath(randomImagePath)
        .build();
  }

  public static List<ReviewImage> createDummyCafeImages(Review review, int size) {
    List<ReviewImage> images = new ArrayList<>();
    for(int i = 0; i < size; i++)
      images.add(createDummyReviewImage(review));
    return images;
  }
}

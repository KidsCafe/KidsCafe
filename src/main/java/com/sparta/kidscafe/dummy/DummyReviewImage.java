package com.sparta.kidscafe.dummy;

import com.sparta.kidscafe.common.util.TestUtil;
import com.sparta.kidscafe.domain.review.entity.Review;
import com.sparta.kidscafe.domain.review.entity.ReviewImage;

import java.util.ArrayList;
import java.util.List;

public class DummyReviewImage {

  public static ReviewImage createDummyReviewImage(Review review) {
    String randomImagePath = "http://..." + TestUtil.getRandomString(10) + ".jpg";
    return ReviewImage.builder()
        .reviewId(review.getId())
        .imagePath(randomImagePath)
        .build();
  }

  public static List<ReviewImage> createDummyCafeImages(Review review, int size) {
    List<ReviewImage> images = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      images.add(createDummyReviewImage(review));
    }
    return images;
  }

  public static ReviewImage createDummyReviewImage(Review review, String path) {
    return ReviewImage.builder()
        .reviewId(review.getId())
        .imagePath(path)
        .build();
  }

  public static List<ReviewImage> createDummyReviewImages(Review review, List<Long> ids) {
    List<ReviewImage> images = new ArrayList<>();
    for (Long id : ids) {
      images.add(createDummyReviewImage(id, review));
    }
    return images;
  }

  public static ReviewImage createDummyReviewImage(Long id, Review review) {
    return ReviewImage.builder()
        .id(id)
        .reviewId(review.getId())
        .imagePath(TestUtil.getRandomString(10) + ".jpg")
        .build();
  }
}

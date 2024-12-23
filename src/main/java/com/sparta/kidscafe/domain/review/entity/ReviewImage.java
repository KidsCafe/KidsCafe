package com.sparta.kidscafe.domain.review.entity;

import com.sparta.kidscafe.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "review_image")
public class ReviewImage extends Timestamped {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private Long reviewId;

  @Column(nullable = false)
  private String imagePath;

  public void deleteReviewId() {
    this.reviewId = null;
  }

  public void updateReviewImages(Long reviewId) {
    this.reviewId = reviewId;
  }
}

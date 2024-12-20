package com.sparta.kidscafe.domain.review.entity;

import com.sparta.kidscafe.common.entity.Timestamped;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.report.entity.Report;
import com.sparta.kidscafe.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "review")
public class Review extends Timestamped {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_review_id")
  private Review parentReview;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cafe_id")
  private Cafe cafe;

  @Column(nullable = false)
  private double star;

  @Column(nullable = false, length = 1000)
  private String content;

  @Column(nullable = false)
  private int depth;

  @Builder.Default
  @OneToMany(mappedBy = "review", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  private List<Report> report = new ArrayList<>();

  @OneToMany(mappedBy = "parentReview", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Review> replies = new ArrayList<>();

  public Review(Long id, User user, Cafe cafe, double star, String content) {
    this.id = id;
    this.user = user;
    this.cafe = cafe;
    this.star = star;
    this.content = content;
  }

  public void updateReview(double star, String content) {
    this.star = star;
    this.content = content;
  }
}

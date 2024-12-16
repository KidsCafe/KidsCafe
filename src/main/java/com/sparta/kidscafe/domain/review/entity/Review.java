package com.sparta.kidscafe.domain.review.entity;

import com.sparta.kidscafe.common.entity.Timestamped;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.report.entity.Report;
import com.sparta.kidscafe.domain.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

  @Column(nullable = false)
  private String imageList;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cafe_id")
  private Cafe cafe;

  @Column(nullable = false)
  private double star;

  @Column(nullable = false)
  private String content;

  @Builder.Default
  @OneToMany(mappedBy = "review", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  private List<Report> report = new ArrayList<>();

  public Review(Long id, User user, Cafe cafe, double star, String content) {
    this.id = id;
    this.user = user;
    this.cafe = cafe;
    this.star = star;
    this.content = content;
  }

  public void UpdateReview(double star, String content) {
    this.star = star;
    this.content = content;
  }
}

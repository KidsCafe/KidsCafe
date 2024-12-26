package com.sparta.kidscafe.domain.review.entity;

import com.sparta.kidscafe.common.entity.Timestamped;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
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
import java.util.Map;
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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cafe_id")
  private Cafe cafe;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_review_id")
  private Review parentReview;

  @OneToMany(mappedBy = "parentReview", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Review> replies = new ArrayList<>();

  @Column(nullable = false)
  private double star;

  @Column(nullable = false, length = 1000)
  private String content;

  @Column(nullable = false)
  private int depth;


  // Map<String, Object> 데이터를 처리하는 생성자 추가
  public Review(Cafe cafe, Map<String, Object> data) {
    this.cafe = cafe;
    this.user = null; // 기본적으로 user는 null로 설정
    this.star = Double.parseDouble(data.get("star").toString());
    this.content = data.get("content").toString();
    this.depth = Integer.parseInt(data.getOrDefault("depth", "0").toString());
  }

  // 리뷰 업데이트 메서드
  public void updateReview(double star, String content) {
    this.star = star;
    this.content = content;
  }
}

package com.sparta.kidscafe.domain.cafe.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class CafeViewCount {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cafe_id", nullable = false)
  private Cafe cafe;

  @Column(nullable = false)
  private Integer viewCount;

  @Builder
  public CafeViewCount(Cafe cafe, Integer viewCount) {
    this.cafe = cafe;
    this.viewCount = viewCount;
  }

  public void incrementViewCount() {
    this.viewCount++;
  }
}

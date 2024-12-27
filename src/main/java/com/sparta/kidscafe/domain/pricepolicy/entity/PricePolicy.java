package com.sparta.kidscafe.domain.pricepolicy.entity;

import com.sparta.kidscafe.common.entity.Timestamped;
import com.sparta.kidscafe.common.enums.TargetType;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "price_policy")
public class PricePolicy extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cafe_id", nullable = false)
  private Cafe cafe;

  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false)
  private TargetType targetType;

  @Column(nullable = false)
  private Long targetId;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String dayType;

  @Column(nullable = false)
  private double rate;

  public void updateDetails(Long targetId, String title, String dayType, Double rate) {
    if (targetId != null) {
      this.targetId = targetId;
    }
    if (title != null) {
      this.title = title;
    }
    if (dayType != null) {
      this.dayType = dayType;
    }
    if (rate != null) {
      this.rate = rate;
    }
  }
}

package com.sparta.kidscafe.domain.coupon.entity;

import com.sparta.kidscafe.common.entity.Timestamped;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "coupon_lock")
public class CouponLock extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String discountType; // 정액, 정률

  @Column(nullable = true)
  private Long discountRate; // 정률 할인 (nullable)

  @Column(nullable = true)
  private Long discountPrice; // 정액 할인 (nullable)

  @Column(nullable = false)
  private Long maxQuantity; // 최대 발급 수량

  @Column(nullable = false)
  private Long issuedQuantity; // 발급된 수량

  @Version
  private Long version; // Optimistic Locking에 사용

  @Column(nullable = false)
  private boolean active; // 쿠폰 활성 상태

  // 생성자
  public CouponLock(String discountType, Long discountRate, Long discountPrice, Long maxQuantity) {
    this.discountType = discountType;
    this.discountRate = discountRate;
    this.discountPrice = discountPrice;
    this.maxQuantity = maxQuantity;
    this.issuedQuantity = 0L;
    this.active = true;
  }

  // 쿠폰 발급 로직
  public void issueCoupon() {
    if (!active) {
      throw new IllegalStateException("이 쿠폰은 비활성화 상태입니다.");
    }
    if (issuedQuantity >= maxQuantity) {
      throw new IllegalStateException("모든 쿠폰이 소진되었습니다.");
    }
    issuedQuantity++;
  }
}


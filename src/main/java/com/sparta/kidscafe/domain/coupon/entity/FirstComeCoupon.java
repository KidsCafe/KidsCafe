package com.sparta.kidscafe.domain.coupon.entity;

import com.sparta.kidscafe.common.entity.Timestamped;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "firstcome_coupon")
public class FirstComeCoupon extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cafe_id")
  private Cafe cafe;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String discountType;

  @Column(nullable = true)
  private Double discountRate;

  @Column(nullable = true)
  private Long discountPrice;

  @Column(nullable = false)
  private Long maxQuantity;

  @Column(nullable = false)
  private Long issuedQuantity;

  @Version
  private Long version;     // 트랜잭션 관리용 컬럼

  @Column(nullable = false)
  private boolean active;

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


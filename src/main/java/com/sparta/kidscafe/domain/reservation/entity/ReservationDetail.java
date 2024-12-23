package com.sparta.kidscafe.domain.reservation.entity;

import com.sparta.kidscafe.common.enums.TargetType;
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
@Table(name = "reservation_detail")
public class ReservationDetail {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "reservation_id")
  private Reservation reservation;

  @Enumerated(value = EnumType.STRING)
  private TargetType targetType;

  @Column(nullable = false)
  private Long targetId;

  @Column(nullable = false)
  private int price;

  @Column(nullable = false)
  private Long count;

  public void updatePrice(int price) {
    this.price = price;
  }

  public void updateCount(Long count) {
    this.count = count;
  }
}

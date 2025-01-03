package com.sparta.kidscafe.domain.reservation.entity;

import com.sparta.kidscafe.common.entity.Timestamped;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.reservation.enums.ReservationStatus;
import com.sparta.kidscafe.domain.user.entity.User;
import com.sparta.kidscafe.exception.BusinessException;
import com.sparta.kidscafe.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
//@Where(clause = "is_deleted = false")
@Entity
@Table(name = "reservation")
public class Reservation extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cafe_id")
  private Cafe cafe;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Getter
  @Enumerated(EnumType.STRING)
  private ReservationStatus status;

  @Column(updatable = false)
  private LocalDateTime startedAt;

  @Column(updatable = false)
  private LocalDateTime finishedAt;

  @Column(updatable = false)
  private int totalPrice;

  private boolean isDeleted;

  private boolean isPaymentConfirmed;

  @Builder.Default
  @OneToMany(mappedBy = "reservation", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  private List<ReservationDetail> reservationDetails = new ArrayList<>();

  public Reservation() {
    this.status = ReservationStatus.PENDING;
    this.isDeleted = false;
    this.isPaymentConfirmed = false;
  }

  public void updateTotalPrice(int price) {
    totalPrice = price;
  }

  public void updateTime(LocalDateTime startedAt, LocalDateTime finishedAt) {
    this.startedAt = startedAt;
    this.finishedAt = finishedAt;
  }

  public void approve() {
    if (this.status != ReservationStatus.PENDING) {
      throw new BusinessException(ErrorCode.INVALID_STATUS);
    }
    this.status = ReservationStatus.APPROVED;
  }

  public void cancelByUser() {
    if (this.status != ReservationStatus.PENDING) {
      throw new BusinessException(ErrorCode.INVALID_STATUS);
    }
    this.status = ReservationStatus.CANCELLED_BY_USER;
    this.isDeleted = true;
  }

  public void cancelByOwner() {
    if (this.status != ReservationStatus.COMPLETED) {
      throw new BusinessException(ErrorCode.INVALID_STATUS);
    }
    this.status = ReservationStatus.CANCELLED_BY_OWNER;
    this.isDeleted = true;
  }

  public Reservation confirmPayment() {
    if (this.status != ReservationStatus.APPROVED) {
      throw new BusinessException(ErrorCode.INVALID_STATUS);
    }
    this.isPaymentConfirmed = true;
    this.status = ReservationStatus.COMPLETED;
    return this;
  }
}

package com.sparta.kidscafe.domain.reservation.entity;

import com.sparta.kidscafe.common.entity.Timestamped;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.reservation.enums.ReservationStatus;
import com.sparta.kidscafe.domain.user.entity.User;
import com.sparta.kidscafe.exception.BusinessException;
import com.sparta.kidscafe.exception.ErrorCode;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

@Getter
@Builder
@AllArgsConstructor
@Where(clause = "is_deleted = false")
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

  private boolean isDeleted;

  @Column(updatable = false)
  private LocalDateTime startedAt;

  @Column(updatable = false)
  private LocalDateTime finishedAt;

  @Column(updatable = false)
  private int totalPrice;

  @Builder.Default
  @OneToMany(mappedBy = "reservation", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  private List<ReservationDetail> reservationDetails = new ArrayList<>();

  public void updateTotalPrice(int price) {
    totalPrice = price;
  }

  public Reservation() {
    this.status = ReservationStatus.PENDING;
    this.isDeleted = false;
  }
  public void approve() {
    if (this.status != ReservationStatus.PENDING) {
      throw new BusinessException(ErrorCode.INVALID_STATUS_CHANGE);
    } this.status = ReservationStatus.APPROVED;
  }

  public void updateStatus(ReservationStatus status) {
    if (status == ReservationStatus.PENDING) {
      throw new BusinessException(ErrorCode.INVALID_STATUS_CHANGE);
    } this.status = status;
  }

  public void cancelByUser() {
    if (this.status != ReservationStatus.PENDING) {
      throw new BusinessException(ErrorCode.INVALID_STATUS_CHANGE);
    } this.status = ReservationStatus.CANCELLED_BY_USER;
  }

  public void cancelByOwner() {
    this.status = ReservationStatus.CANCELLED_BY_OWNER;
    this.isDeleted = true;
  }

  public boolean isDeleted() {
    return isDeleted;
  }
}

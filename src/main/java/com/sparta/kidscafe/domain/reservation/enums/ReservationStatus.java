package com.sparta.kidscafe.domain.reservation.enums;

import lombok.Getter;

@Getter
public enum ReservationStatus {
  PENDING(false, true),
  APPROVED(false, true),
  COMPLETED(false, false),
  CANCELLED_BY_USER(true, false),
  CANCELLED_BY_OWNER(true, false);

  private final boolean isCancelled;

  @Getter
  private final boolean canBeCancelledByOwner;

  ReservationStatus(boolean isCancelled, boolean canBeCancelledByOwner) {
    this.isCancelled = isCancelled;
    this.canBeCancelledByOwner = canBeCancelledByOwner;
  }

  public boolean isCancelled() {
    return isCancelled;
  }
}

package com.sparta.kidscafe.domain.reservation.enums;

import lombok.Getter;

@Getter
public enum ReservationStatus {
  PENDING(false, true, true),
  APPROVED(false, true, true),
  COMPLETED(false, false, false),
  CANCELLED_BY_USER(true, false, false),
  CANCELLED_BY_OWNER(true, false, false),
  ;

  private final boolean isCancelled;
  private final boolean canBeCancelledByUser;
  private final boolean canBeCancelledByOwner;

  ReservationStatus(boolean isCancelled, boolean canBeCancelledByUser,
                    boolean canBeCancelledByOwner) {
    this.isCancelled = isCancelled;
    this.canBeCancelledByUser = canBeCancelledByUser;
    this.canBeCancelledByOwner = canBeCancelledByOwner;
  }

}

package com.sparta.kidscafe.domain.coupon.repository;

import com.sparta.kidscafe.domain.coupon.entity.CouponLock;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CouponLockRepository extends JpaRepository<CouponLock, Long> {

  @Lock(LockModeType.OPTIMISTIC)
  @Query("SELECT c FROM CouponLock c WHERE c.id = :id")
  Optional<CouponLock> findByIdWithLock(@Param("id") Long id);

}

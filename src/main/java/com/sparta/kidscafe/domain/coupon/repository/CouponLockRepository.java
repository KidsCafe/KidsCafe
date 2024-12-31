package com.sparta.kidscafe.domain.coupon.repository;

import com.sparta.kidscafe.domain.coupon.entity.CouponLock;
import jakarta.persistence.QueryHint;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponLockRepository extends JpaRepository<CouponLock, Long> {
  @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "1000")})
  Optional<CouponLock> findById(Long id);
}

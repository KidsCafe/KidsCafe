package com.sparta.kidscafe.domain.coupon.repository;

import com.sparta.kidscafe.domain.coupon.entity.FirstComeCoupon;
import jakarta.persistence.QueryHint;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

@Repository
public interface FirstComeCouponRepository extends JpaRepository<FirstComeCoupon, Long> {
  @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "1000")})
  Optional<FirstComeCoupon> findById(Long id);
}

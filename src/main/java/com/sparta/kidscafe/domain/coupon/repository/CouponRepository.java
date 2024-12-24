package com.sparta.kidscafe.domain.coupon.repository;

import com.sparta.kidscafe.domain.coupon.entity.Coupon;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

  List<Coupon> findByCafeId(Long cafeId);

  List<Coupon> findByUserId(Long userId);
}

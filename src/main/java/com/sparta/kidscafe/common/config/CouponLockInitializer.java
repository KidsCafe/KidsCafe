package com.sparta.kidscafe.common.config;

import com.sparta.kidscafe.domain.coupon.entity.CouponLock;
import com.sparta.kidscafe.domain.coupon.repository.CouponLockRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CouponLockInitializer {

  @Bean
  public CommandLineRunner initialize(CouponLockRepository couponLockRepository) {
    return args -> {
      CouponLock coupon = new CouponLock("정액", null, 1000L, 100L);
      couponLockRepository.save(coupon);
    };
  }
}

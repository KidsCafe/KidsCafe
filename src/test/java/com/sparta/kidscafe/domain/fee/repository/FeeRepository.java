package com.sparta.kidscafe.domain.fee.repository;

import com.sparta.kidscafe.domain.fee.entity.Fee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeeRepository extends JpaRepository<Fee, Long> {
  List<Fee> findAllByCafeId(Long cafeId);
}

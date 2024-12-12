package com.sparta.kidscafe.domain.fee.repository;

import com.sparta.kidscafe.domain.fee.entity.Fee;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeeRepository extends JpaRepository<Fee, Long> {
  List<Fee> findAllByCafeId(Long cafeId);
}

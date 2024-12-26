package com.sparta.kidscafe.domain.fee.repository;

import com.sparta.kidscafe.domain.fee.entity.Fee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeeRepository extends JpaRepository<Fee, Long> {

  // 카페 상세 정보에 사용하는 쿼리 메소드
  List<Fee> findAllByCafeId(Long cafeId);

  // 요금표 생성 사용하는 쿼리 메소드
  List<Fee> findByCafeId(Long cafeId);
}

package com.sparta.kidscafe.domain.cafe.repository;

import com.sparta.kidscafe.domain.cafe.entity.CafeImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CafeImageRepository extends JpaRepository<CafeImage, Long> {

  List<CafeImage> findAllByCafeId(Long cafeId);

  void deleteByCafeId(Long cafeId);

  boolean existsByCafeId(Long cafeId);
}
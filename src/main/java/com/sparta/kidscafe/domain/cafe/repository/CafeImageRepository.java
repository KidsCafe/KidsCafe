package com.sparta.kidscafe.domain.cafe.repository;

import com.sparta.kidscafe.domain.cafe.entity.CafeImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CafeImageRepository extends JpaRepository<CafeImage, Long> {

  List<CafeImage> findAllByCafeId(Long cafeId);

  Long countByCafeId(Long cafeId);
}

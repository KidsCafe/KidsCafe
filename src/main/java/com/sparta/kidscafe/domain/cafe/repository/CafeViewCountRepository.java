package com.sparta.kidscafe.domain.cafe.repository;

import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.entity.CafeViewCount;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CafeViewCountRepository extends JpaRepository<CafeViewCount, Long> {

  Optional<CafeViewCount> findByCafe(Cafe cafe);

  // 지역별 모든 CafeViewCount 조회
  @Query("SELECT c FROM CafeViewCount c WHERE c.cafe.region LIKE :region% ORDER BY c.viewCount DESC")
  List<CafeViewCount> findAllByRegion(@Param("region") String region);

  // 지역별 상위 N개(페이지) 조회
  @Query(value = "SELECT c FROM CafeViewCount c WHERE c.cafe.region LIKE :region% ORDER BY c.viewCount DESC")
  Page<CafeViewCount> findCafesByRegionOrderByViewCountDesc(@Param("region") String region, Pageable pageable);

  List<CafeViewCount> findAllByCafeIdInOrderByViewCountDesc(List<Long> cafeIds);
}

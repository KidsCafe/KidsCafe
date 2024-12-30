package com.sparta.kidscafe.domain.cafe.repository;

import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.entity.CafeViewCount;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CafeViewCountRepository extends JpaRepository<CafeViewCount, Long> {

  Optional<CafeViewCount> findByCafe(Cafe cafe);

  // Redis용
  @Query("SELECT c FROM CafeViewCount c WHERE c.cafe.region = :region ORDER BY c.viewCount DESC")
  List<CafeViewCount> findAllByRegion(@Param("region") String region);

  // DB용
  @Query("SELECT c FROM CafeViewCount c WHERE c.cafe.region = :region ORDER BY c.viewCount DESC limit 5")
  List<CafeViewCount> findTop5Cafe_RegionOrderByViewCountDesc(String region);

  List<CafeViewCount> findAllByCafeIdInOrderByViewCountDesc(List<Long> cafeIds);
}

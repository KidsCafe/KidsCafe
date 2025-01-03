package com.sparta.kidscafe.domain.cafe.repository;

import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CafeRepository extends JpaRepository<Cafe, Long>, CafeDslRepository {

  boolean existsByIdAndUserId(Long id, Long userId);

  Optional<Cafe> findByIdAndUserId(Long id, Long userId);

  List<Cafe> findAllByUserIdAndIdIn(Long userId, List<Long> ids);

  List<Cafe> findAllByRegion(String region);

  Long countByUserId(Long userId);
}
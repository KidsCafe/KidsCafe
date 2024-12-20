package com.sparta.kidscafe.domain.cafe.repository;

import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CafeRepository extends JpaRepository<Cafe, Long>, CafeDslRepository {

  boolean existsByIdAndUserId(Long id, Long userId);

  Optional<Cafe> findByIdAndUserId(Long id, Long userId);

  List<Cafe> findAllByUserIdAndIdIn(Long userId, List<Long> ids);
}

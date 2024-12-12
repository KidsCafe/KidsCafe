package com.sparta.kidscafe.domain.cafe.repository;

import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CafeRepository extends JpaRepository<Cafe, Long>, CafeDslRepository {

  boolean existsByIdAndUserId(Long id, Long userId);

  Optional<Cafe> findByIdAndUserId(Long id, Long userId);
}

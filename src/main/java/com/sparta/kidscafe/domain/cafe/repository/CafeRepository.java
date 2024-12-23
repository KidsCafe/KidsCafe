package com.sparta.kidscafe.domain.cafe.repository;

import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CafeRepository extends JpaRepository<Cafe, Long>, CafeDslRepository {

  boolean existsByIdAndUserId(Long id, Long userId);

  Optional<Cafe> findByIdAndUserId(Long id, Long userId);

  List<Cafe> findAllByUserIdAndIdIn(Long userId, List<Long> ids);

//  @Query("Select new dto(c.id, c.name, c.address) From Cafe c Where c.name Like %:keyword%")
//  List<Cafe> findByKeyword(@Param("keyword") String keyword);

//  Page<Cafe> findByNameContaining(String name, Pageable pageable);
}

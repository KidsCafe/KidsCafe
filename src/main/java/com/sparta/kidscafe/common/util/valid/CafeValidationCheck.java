package com.sparta.kidscafe.common.util.valid;

import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.exception.BusinessException;
import com.sparta.kidscafe.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CafeValidationCheck {
  private final CafeRepository cafeRepository;

  public Cafe validMyCafe(Long cafeId, Long userId) {
    return cafeRepository.findByIdAndUserId(cafeId, userId)
        .orElseThrow(() -> new BusinessException(ErrorCode.CAFE_NOT_FOUND));
  }
}

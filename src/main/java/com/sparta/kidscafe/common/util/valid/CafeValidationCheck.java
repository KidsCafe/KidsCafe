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
  private final Long CAFE_MAXIMUM_COUNT = 20L;
  private final CafeRepository cafeRepository;

  public Cafe findCafe(Long id){
    return cafeRepository.findById(id)
        .orElseThrow(() -> new BusinessException(ErrorCode.CAFE_NOT_FOUND));
  }

  public Cafe validMyCafe(Long cafeId, Long userId) {
    return cafeRepository.findByIdAndUserId(cafeId, userId)
        .orElseThrow(() -> new BusinessException(ErrorCode.CAFE_NOT_FOUND));
  }

  public void validOverMaximum(Long userId) {
    Long count = cafeRepository.countByUserId(userId);
    if(count >= CAFE_MAXIMUM_COUNT)
      throw new BusinessException(ErrorCode.CAFE_OVER_MAXIMUM);
  }
}

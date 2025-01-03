package com.sparta.kidscafe.domain.cafe.service;

import com.sparta.kidscafe.common.dto.ResponseDto;
import com.sparta.kidscafe.domain.cafe.dto.response.CafeViewCountResponseDto;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.entity.CafeViewCount;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.domain.cafe.repository.CafeViewCountRepository;
import com.sparta.kidscafe.exception.BusinessException;
import com.sparta.kidscafe.exception.ErrorCode;
import java.util.Optional;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Builder
@Service
@Getter
@RequiredArgsConstructor
public class CafeViewCountService {

  private final CafeViewCountRepository cafeViewCountRepository;
  private final CafeRepository cafeRepository;
  private final RedisTemplate<String, Object> redisTemplate;

  /**
   * 특정 카페의 조회수를 +1 DB 저장 & Redis Sorted Set 반영
   */
  public int incrementViewCount(Long cafeId) {
    Cafe cafe = cafeRepository.findById(cafeId)
        .orElseThrow(() -> new BusinessException(ErrorCode.CAFE_NOT_FOUND));

    CafeViewCount cafeViewCount = cafeViewCountRepository.findByCafe(cafe)
        .orElseGet(() -> cafeViewCountRepository.save(
            CafeViewCount
                .builder()
                .cafe(cafe)
                .viewCount(0)
                .build()));

    cafeViewCount.incrementViewCount();
    cafeViewCountRepository.save(cafeViewCount);

    // Redis 캐시 갱신
    String redisKey = "popular-cafes:" + cafe.getRegion();
    redisTemplate.opsForZSet().incrementScore(redisKey, cafe.getId().toString(), 1);
    return cafeViewCount.getViewCount();
  }

  public ResponseDto<CafeViewCountResponseDto> getViewCount(Long cafeId) {
    Cafe cafe = cafeRepository.findById(cafeId)
        .orElseThrow(() -> new BusinessException(ErrorCode.CAFE_NOT_FOUND));
    int viewCount = 0;
    Optional<CafeViewCount> cafeViewCount = cafeViewCountRepository.findByCafe(cafe);
    if (cafeViewCount.isPresent()) {
      viewCount = cafeViewCount.get().getViewCount();
    }
    CafeViewCountResponseDto responseDto = CafeViewCountResponseDto.from(cafe, viewCount);
    return ResponseDto.create(responseDto);
  }
}

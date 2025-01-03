package com.sparta.kidscafe.domain.cafe.service;

import com.sparta.kidscafe.common.dto.ListResponseDto;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.domain.cafe.dto.response.CafeRankingResponseDto;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.entity.CafeViewCount;
import com.sparta.kidscafe.domain.cafe.repository.CafeViewCountRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CafeRankingService {

  private final RedisTemplate<String, Object> redisTemplate;
  private final CafeViewCountRepository cafeViewCountRepository;

  private static final String RANKING_KEY_PREFIX = "popular-ranking:";

  /**
   * DB에서 region의 상위 10개 카페 조회 -> Redis Sorted Set에 저장
   */
  public StatusDto refreshTopN(String region, int n) {
    Page<CafeViewCount> page = cafeViewCountRepository.findCafesByRegionOrderByViewCountDesc(
        region, PageRequest.of(0, n));
    List<CafeViewCount> topN = page.getContent();
    if (topN.isEmpty()) {
      return StatusDto.builder()
          .status(HttpStatus.NO_CONTENT.value())
          .message("해당 지역(" + region + ")에 데이터가 없습니다.")
          .build();
    }

    String redisKey = RANKING_KEY_PREFIX + region;
    redisTemplate.opsForZSet().removeRange(redisKey, 0, -1); // 기존 데이터 삭제

    for (CafeViewCount cafeViewCount : topN) {
      Cafe cafe = cafeViewCount.getCafe();
      int viewCount = cafeViewCount.getViewCount() == null ? 0 : cafeViewCount.getViewCount();

      CafeRankingResponseDto responseDto = CafeRankingResponseDto.builder()
          .cafeId(cafe.getId())
          .cafeName(cafe.getName())
          .viewCount(viewCount)
          .build();

      redisTemplate.opsForZSet().add(redisKey, responseDto, viewCount);
    }
    return StatusDto.builder()
        .status(HttpStatus.OK.value())
        .message(region + " 지역 인기 카페 갱신 성공")
        .build();
  }

  /**
   * Redis에서 region의 상위 10개 카페 조회 -> 없으면 DB 갱신 후 다시 조회
   */
  public ListResponseDto<CafeRankingResponseDto> getTopN(String region, int n) {
    String redisKey = RANKING_KEY_PREFIX + region;
    Set<ZSetOperations.TypedTuple<Object>> set =
        redisTemplate.opsForZSet().reverseRangeWithScores(redisKey, 0, n - 1);

    if (set == null || set.isEmpty()) {
      refreshTopN(region, n);
      set = redisTemplate.opsForZSet().reverseRangeWithScores(redisKey, 0, n - 1);
      if (set == null) {
        return ListResponseDto.create(Collections.emptyList(),
            region + " 지역에 데이터가 없습니다.(Redis Null)");
      }
    }
    List<CafeRankingResponseDto> responseDto = new ArrayList<>();
    for (ZSetOperations.TypedTuple<Object> tuple : set) {
      Object object = tuple.getValue();
      if (object instanceof CafeRankingResponseDto dto) {
        dto.setViewCount(tuple.getScore().intValue());
        responseDto.add(dto);
      }
    }
    return ListResponseDto.create(responseDto, region + " 지역 인기 카페 조회 성공");
  }
}

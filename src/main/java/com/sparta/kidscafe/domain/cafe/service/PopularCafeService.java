package com.sparta.kidscafe.domain.cafe.service;

import com.sparta.kidscafe.common.dto.ListResponseDto;
import com.sparta.kidscafe.domain.cafe.dto.response.CafeViewCountResponseDto;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.entity.CafeViewCount;
import com.sparta.kidscafe.domain.cafe.repository.CafeViewCountRepository;
import com.sparta.kidscafe.domain.lesson.repository.LessonRepository;
import com.sparta.kidscafe.domain.room.repository.RoomRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PopularCafeService {

  private final RedisTemplate<String, Object> redisTemplate;
  private final CafeViewCountRepository cafeViewCountRepository;

  private static final String POPULAR_CAFE_KEY = "popular-cafes";


  // DB에서 조회
  public ListResponseDto<CafeViewCountResponseDto> getTopCafesFromDB(String region) {
    Page<CafeViewCount> page = cafeViewCountRepository.findCafesByRegionOrderByViewCountDesc(
        region, PageRequest.of(0, 10));
    List<CafeViewCount> cafeViewCounts = page.getContent();
    if (cafeViewCounts.isEmpty()) {
      return ListResponseDto.create(List.of(), "조회수가 0입니다.");
    }
    List<CafeViewCountResponseDto> responseDto = cafeViewCounts.stream()
        .map(cvc -> CafeViewCountResponseDto.from(cvc.getCafe(), cvc.getViewCount()))
        .collect(Collectors.toList());
    return ListResponseDto.create(responseDto, region + " 인기 카페 조회 성공");
  }

  /**
   * Redis에서 지역별 상위 5개 카페 조회 - Redis에 데이터가 없으면 DB에서 조회 후 Redis에 저장
   */
  public ListResponseDto<CafeViewCountResponseDto> getTopCafesFromRedis(String region) {
    String redisKey = POPULAR_CAFE_KEY + region.trim();

    // Redis에서 상위 5개 ID
    Set<Object> topCafeIds = redisTemplate.opsForZSet().reverseRange(redisKey, 0, 9);

    // Redis에 데이터가 없으면 DB에서 조회 후 Redis에 저장
    if (topCafeIds == null || topCafeIds.isEmpty()) {
      Page<CafeViewCount> page = cafeViewCountRepository.findCafesByRegionOrderByViewCountDesc(
          region, PageRequest.of(0, 10));
      List<CafeViewCount> dbTop5 = page.getContent();

      if (dbTop5.isEmpty()) {
        return ListResponseDto.create(Collections.emptyList(), region + " 지역에 데이터가 없습니다.");
      }
      // Redis에 추가
      for (CafeViewCount cafeViewCount : dbTop5) {
        redisTemplate.opsForZSet().add(redisKey, cafeViewCount.getCafe().getId().toString(),
            cafeViewCount.getViewCount()
        );
      }
      // 다시 Redis에서 조회
      topCafeIds = redisTemplate.opsForZSet().reverseRange(redisKey, 0, 9);
      if (topCafeIds == null) {
        return ListResponseDto.create(Collections.emptyList(),
            region + " 지역에 데이터가 없습니다. (Redis null");
      }
    }

    List<Long> cafeIds = topCafeIds.stream()
        .map(id -> Long.parseLong(id.toString()))
        .toList();
    List<CafeViewCount> cafeViewCounts = cafeViewCountRepository.findAllByCafeIdInOrderByViewCountDesc(
        cafeIds);

    if (cafeViewCounts.isEmpty()) {
      return ListResponseDto.create(Collections.emptyList(), region + " 지역에 해당하는 카페 데이터가 없습니다.");
    }

    // 반환
    List<CafeViewCountResponseDto> cafeResponseList = cafeViewCounts.stream()
        .map(cvc -> CafeViewCountResponseDto.from(cvc.getCafe(), cvc.getViewCount()))
        .collect(Collectors.toList());
    return ListResponseDto.create(cafeResponseList, region + " 지역 인기 카페 조회 성공");
  }

  // 지역별 Redis 캐시 전체 초기화(선택)
  public void updateCafeViewsInRedis(String region) {
    String redisKey = POPULAR_CAFE_KEY + ":" + region;

    List<CafeViewCount> cafeViewCounts = cafeViewCountRepository.findAllByRegion(region);

    redisTemplate.opsForZSet().removeRange(redisKey, 0, -1);
    for (CafeViewCount cafeViewCount : cafeViewCounts) {
      redisTemplate.opsForZSet()
          .add(redisKey, cafeViewCount.getCafe().getId().toString(),
              cafeViewCount.getViewCount());
    }
  }
}

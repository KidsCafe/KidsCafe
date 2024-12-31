package com.sparta.kidscafe.domain.cafe.service;

import com.sparta.kidscafe.common.dto.ListResponseDto;
import com.sparta.kidscafe.domain.cafe.dto.response.CafeViewCountResponseDto;
import com.sparta.kidscafe.domain.cafe.entity.CafeViewCount;
import com.sparta.kidscafe.domain.cafe.repository.CafeViewCountRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
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
    List<CafeViewCount> cafeViewCounts = cafeViewCountRepository.findTop5Cafe_RegionOrderByViewCountDesc(
        region);
    if (cafeViewCounts.isEmpty()) {
      return ListResponseDto.create(List.of(), "조회수가 0입니다.");
    }
    List<CafeViewCountResponseDto> responseDto = new ArrayList<>();
    for (CafeViewCount cafeViewCount : cafeViewCounts) {
      responseDto.add(
          CafeViewCountResponseDto.from(cafeViewCount.getCafe(), cafeViewCount.getViewCount()));
    }
    return ListResponseDto.create(responseDto, region + " 인기 카페 조회 성공");
  }

  // Redis에서 지역별 Top 5 조회
  public ListResponseDto<CafeViewCountResponseDto> getTopCafesFromRedis(String region) {
    String redisKey = "popular-cafes:" + region.trim();
    System.out.println("Redis Key: " + redisKey);

    Set<Object> topCafeIds = redisTemplate.opsForZSet().reverseRange(redisKey, 0, 4);
    System.out.println("Redis 조회 결과: " + topCafeIds);
    if (topCafeIds == null || topCafeIds.isEmpty()) {
      return ListResponseDto.create(List.of(), "해당 지역에 데이터가 없습니다.");
    }
    List<Long> cafeIds = topCafeIds.stream()
        .map(id -> Long.parseLong(id.toString()))
        .toList();
    System.out.println("DB에서 조회할 카페 ID 리스트: " + cafeIds);
    List<CafeViewCount> cafeViewCounts = cafeViewCountRepository.findAllByCafeIdInOrderByViewCountDesc(
        cafeIds);
    System.out.println("DB 조회 결과: " + cafeViewCounts);
    List<CafeViewCountResponseDto> cafeResponseList = cafeViewCounts.stream()
        .map(cv -> CafeViewCountResponseDto.from(cv.getCafe(), cv.getViewCount()))
        .toList();
    return ListResponseDto.create(cafeResponseList, "인기 카페 조회 성공");
  }

  // Redis에 지역별 카페 조회수 저장
  public void updateCafeViewsInRedis(String region) {
    String redisKey = "popular-cafes" + ":" + region;

    List<CafeViewCount> cafeViewCounts = cafeViewCountRepository.findAllByRegion(region);

    redisTemplate.opsForZSet().removeRange(redisKey, 0, -1);
    for (CafeViewCount cafeViewCount : cafeViewCounts) {
      redisTemplate.opsForZSet()
          .add(redisKey, cafeViewCount.getCafe().getId().toString(), cafeViewCount.getViewCount());
    }
  }
}

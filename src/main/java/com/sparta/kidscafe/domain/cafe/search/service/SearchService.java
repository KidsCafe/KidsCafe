//package com.sparta.kidscafe.domain.cafe.search.service;
//
//import com.sparta.kidscafe.common.dto.ListResponseDto;
//import com.sparta.kidscafe.domain.cafe.dto.response.CafeSearchResponseDto;
//import com.sparta.kidscafe.domain.cafe.entity.Cafe;
//import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class SearchService {
//
//  private final CafeRepository cafeRepository;
//  private final RedisTemplate<String, Object> redisTemplate;
//
//  // v1: DB 직접 조회
//  public ListResponseDto<CafeSearchResponseDto> searchV1(String keyword) {
//    List<CafeSearchResponseDto> cafes = cafeRepository.findByKeyword(keyword).stream()
//        .map(cafe -> new CafeSearchResponseDto(cafe.getId(),cafe.getName(), cafe.getAddress()))
//        .toList();
//    return ListResponseDto.success(cafes, HttpStatus.OK,"카페 검색에 성공했습니다.");
//  }
//
//}

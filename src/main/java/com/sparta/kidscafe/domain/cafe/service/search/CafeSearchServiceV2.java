//package com.sparta.kidscafe.domain.cafe.service.search;
//
//import com.sparta.kidscafe.common.dto.PageResponseDto;
//import com.sparta.kidscafe.domain.cafe.dto.response.CafeSearchResponseDto;
//import com.sparta.kidscafe.domain.cafe.entity.Cafe;
//import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//
//@Service
//public class CafeSearchServiceV2 {
//
//  private final Map<String, Page<CafeSearchResponseDto>> cache = new ConcurrentHashMap<>();
//
//  private CafeRepository cafeRepository;
//
//  public PageResponseDto<CafeSearchResponseDto> searchCafeV2(String name, int page, int size) {
//    String cacheKey = name + ":" + page + ":" + size;
//
//    // 캐시에서 결과 조회
//    if (cache.containsKey(cacheKey)) {
//      Page<CafeSearchResponseDto> cachedResult = cache.get(cacheKey);
//      return PageResponseDto.success(cachedResult, HttpStatus.OK,"inMemory 캐싱된 카페 검색 성공");
//    }
//    Pageable pageable = PageRequest.of(page, size);
//    Page<Cafe> cafePage = cafeRepository.findByNameContaining(name, pageable);
//
//    Page<CafeSearchResponseDto> cafes = cafePage.map(cafe -> new CafeSearchResponseDto(
//        cafe.getId(),
//        cafe.getName(),
//        cafe.getAddress()
//    ));
//    cache.put(cacheKey, cafes);
//    return PageResponseDto.success(cafes, HttpStatus.OK, "In-Memory 캐싱 후 카페 검색 성공");
//  }
//
//}

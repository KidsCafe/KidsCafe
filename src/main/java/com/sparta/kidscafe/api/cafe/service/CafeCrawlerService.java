package com.sparta.kidscafe.api.cafe.service;

import com.sparta.kidscafe.api.naver.response.NaverApiResponse;
import com.sparta.kidscafe.api.naver.service.NaverApiService;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CafeCrawlerService {

  private static final Logger logger = LoggerFactory.getLogger(CafeCrawlerService.class);

  private final NaverApiService naverApiService;
  private final CafeRepository cafeRepository;
  private final GeometryFactory geometryFactory;

  public CafeCrawlerService(NaverApiService naverApiService, CafeRepository cafeRepository) {
    this.naverApiService = naverApiService;
    this.cafeRepository = cafeRepository;
    this.geometryFactory = new GeometryFactory();
  }

  // 특정 지역 크롤링 및 저장
  public void crawlCafesByRegion(String city, String district) {
    String keyword = city + " " + district + " 키즈카페";
    int start = 1;
    int display = 10; // 한 번에 가져올 데이터 개수

    while (start <= 20) { // 최대 20개까지 요청
      try {
        // Naver API 호출
        NaverApiResponse response = naverApiService.searchLocal(keyword, display, start, "random");

        if (response.getItems() == null || response.getItems().isEmpty()) {
          logger.warn("No cafes found for region: {} {}, start: {}", city, district, start);
          break;
        }

        // 응답 데이터를 Cafe 엔티티로 변환
        List<Cafe> cafeEntities = response.getItems().stream()
            .map(item -> mapToCafeEntity(item, city + " " + district))
            .collect(Collectors.toList());

        // 변환된 데이터를 저장
        if (!cafeEntities.isEmpty()) {
          cafeRepository.saveAll(cafeEntities);
          logger.info("Saved {} cafes for region: {} {}, start: {}", cafeEntities.size(), city, district, start);
        } else {
          logger.warn("No valid cafes to save for region: {} {}, start: {}", city, district, start);
        }

        start += display; // 다음 페이지 요청
      } catch (Exception e) {
        logger.error("Error during cafe crawling for region: {} {}, start: {}, error: {}", city, district, start, e.getMessage(), e);
        break;
      }
    }
  }

  // Cafe 엔티티로 매핑
  private Cafe mapToCafeEntity(NaverApiResponse.Item item, String region) {
    try {
      // 좌표 변환
      Point location = createPoint(item.getMapx(), item.getMapy());

      // Cafe 엔티티 생성
      return Cafe.builder()
          .name(item.getTitle().replaceAll("<[^>]*>", "")) // HTML 태그 제거
          .hyperlink(item.getLink())
          .address(item.getAddress())
          .location(location)
          .region(region)
          .openedAt(java.time.LocalTime.of(9, 0)) // 기본 오픈 시간
          .closedAt(java.time.LocalTime.of(21, 0)) // 기본 닫는 시간
          .size(500) // 기본 크기 설정
          .parking(false) // 기본값 주차 불가능 설정
          .multiFamily(false) // 기본값 설정
          .restaurant(false) // 기본값 설정
          .careService(false) // 기본값 설정
          .swimmingPool(false) // 기본값 설정
          .clothesRental(false) // 기본값 설정
          .monitoring(false) // 기본값 설정
          .feedingRoom(false) // 기본값 설정
          .outdoorPlayground(false) // 기본값 설정
          .safetyGuard(false) // 기본값 설정
          .build();
    } catch (Exception e) {
      logger.error("Error mapping cafe item to entity: {}, error: {}", item, e.getMessage());
      return null;
    }
  }

  // 좌표 생성ㅣ
  private Point createPoint(String mapx, String mapy) {
    try {
      double longitude = Double.parseDouble(mapx) / 1_000_000.0; // 좌표는 소수점 형식으로 변환
      double latitude = Double.parseDouble(mapy) / 1_000_000.0;
      return geometryFactory.createPoint(new Coordinate(longitude, latitude));
    } catch (Exception e) {
      logger.warn("Invalid coordinates: mapx={}, mapy={}", mapx, mapy);
      return null;
    }
  }
}
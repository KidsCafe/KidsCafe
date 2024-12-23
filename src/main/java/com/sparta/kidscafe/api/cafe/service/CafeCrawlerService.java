package com.sparta.kidscafe.api.cafe.service;

import com.sparta.kidscafe.common.service.NaverApiService;
import com.sparta.kidscafe.domain.cafe.dto.response.CafeResponseDto;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CafeCrawlerService {

  private final NaverApiService naverApiService;
  private final CafeRepository cafeRepository;
  private final GeometryFactory geometryFactory;

  @Autowired
  public CafeCrawlerService(NaverApiService naverApiService, CafeRepository cafeRepository) {
    this.naverApiService = naverApiService;
    this.cafeRepository = cafeRepository;
    this.geometryFactory = new GeometryFactory();
  }

  /**
   * 부산시의 모든 구에 대해 키즈카페 데이터를 크롤링합니다.
   */
  public void crawlCafesByDistrict() {
    String[] districts = {
        "중구", "서구", "동구", "영도구", "부산진구", "동래구", "남구",
        "북구", "해운대구", "사하구", "금정구", "강서구", "연제구", "수영구", "사상구"
    };

    for (String district : districts) {
      crawlCafesByDistrict(district);
    }
  }

  /**
   * 특정 구에 대해 키즈카페 데이터를 크롤링합니다.
   *
   * @param district 구 이름
   */
  public void crawlCafesByDistrict(String district) {
    String keyword = district + " 키즈카페";
    Map<String, Object> searchResult = naverApiService.searchLocal(keyword);

    Object itemsObject = searchResult.get("items");
    if (!(itemsObject instanceof List<?>)) {
      throw new IllegalArgumentException("네이버 API의 items 형식이 올바르지 않습니다.");
    }

    @SuppressWarnings("unchecked")
    List<Map<String, Object>> cafes = (List<Map<String, Object>>) itemsObject;

    List<Cafe> cafeEntities = new ArrayList<>();
    for (Map<String, Object> cafeData : cafes) {
      Cafe cafe = mapToCafeEntity(cafeData, district);
      cafeEntities.add(cafe);
    }

    cafeRepository.saveAll(cafeEntities);
  }

  /**
   * 저장된 모든 키즈카페 데이터를 조회합니다.
   *
   * @return CafeResponseDto 리스트
   */
  public List<CafeResponseDto> getAllCafes() {
    List<Cafe> cafes = cafeRepository.findAll();
    List<CafeResponseDto> responseDtos = new ArrayList<>();
    for (Cafe cafe : cafes) {
      responseDtos.add(mapToResponseDto(cafe));
    }
    return responseDtos;
  }

  /**
   * 특정 카페 정보를 조회합니다.
   *
   * @param cafeId 카페 ID
   * @return CafeResponseDto
   */
  public CafeResponseDto getCafeDetails(Long cafeId) {
    Cafe cafe = cafeRepository.findById(cafeId)
        .orElseThrow(() -> new IllegalArgumentException("해당 카페를 찾을 수 없습니다."));
    return mapToResponseDto(cafe);
  }

  /**
   * 네이버 API 데이터를 Cafe 엔티티로 매핑합니다.
   *
   * @param cafeData 네이버 API에서 가져온 데이터
   * @param district 구 이름
   * @return Cafe 엔티티
   */
  private Cafe mapToCafeEntity(Map<String, Object> cafeData, String district) {
    String address = (String) cafeData.get("roadAddress");
    String name = cafeData.get("title").toString().replaceAll("<[^>]*>", "");

    Point location = null;
    if (cafeData.containsKey("mapx") && cafeData.containsKey("mapy")) {
      double longitude = Double.parseDouble(cafeData.get("mapx").toString());
      double latitude = Double.parseDouble(cafeData.get("mapy").toString());
      location = geometryFactory.createPoint(new Coordinate(longitude, latitude));
    }

    return Cafe.builder()
        .name(name)
        .region(district)
        .address(address)
        .location(location)
        .size(500) // 기본값
        .dayOff("주말") // 기본값
        .parking(true) // 기본값
        .restaurant(false) // 기본값
        .careService(false) // 기본값
        .swimmingPool(false) // 기본값
        .clothesRental(false) // 기본값
        .monitoring(false) // 기본값
        .feedingRoom(false) // 기본값
        .outdoorPlayground(false) // 기본값
        .safetyGuard(false) // 기본값
        .hyperlink((String) cafeData.get("link"))
        .openedAt(java.time.LocalTime.of(9, 0))
        .closedAt(java.time.LocalTime.of(21, 0))
        .build();
  }

  /**
   * Cafe 엔티티를 CafeResponseDto로 변환합니다.
   *
   * @param cafe Cafe 엔티티
   * @return CafeResponseDto
   */
  private CafeResponseDto mapToResponseDto(Cafe cafe) {
    return CafeResponseDto.builder()
        .id(cafe.getId())
        .name(cafe.getName())
        .address(cafe.getAddress())
        .longitude(cafe.getLocation() != null ? cafe.getLocation().getX() : null)
        .latitude(cafe.getLocation() != null ? cafe.getLocation().getY() : null)
        .size(cafe.getSize())
        .dayOff(cafe.getDayOff())
        .parking(cafe.isParking())
        .hyperLink(cafe.getHyperlink())
        .openedAt(cafe.getOpenedAt())
        .closedAt(cafe.getClosedAt())
        .build();
  }
}
package com.sparta.kidscafe.api.cafe.service;

import com.sparta.kidscafe.common.service.NaverApiService;
import com.sparta.kidscafe.domain.cafe.dto.response.CafeResponseDto;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

  public void crawlCafesNationwide() {
    String[][] regions = {
        {"서울특별시", "강남구", "서초구", "송파구", "강북구"},
        {"부산광역시", "중구", "서구", "동구", "영도구"},
        {"대구광역시", "중구", "동구", "서구", "남구"},
        {"인천광역시", "중구", "동구", "남구", "부평구"},
        {"대전광역시", "동구", "중구", "서구", "유성구"},
        {"광주광역시", "동구", "서구", "남구", "북구"},
        {"울산광역시", "중구", "남구", "동구", "북구"},
        {"세종특별자치시"},
        {"경기도", "수원시", "성남시", "의정부시", "안양시"},
        {"강원도", "춘천시", "원주시", "강릉시"},
        {"충청북도", "청주시", "충주시", "제천시"},
        {"충청남도", "천안시", "공주시", "보령시"},
        {"전라북도", "전주시", "군산시", "익산시"},
        {"전라남도", "목포시", "여수시", "순천시"},
        {"경상북도", "포항시", "경주시", "구미시"},
        {"경상남도", "창원시", "진주시", "통영시"},
        {"제주특별자치도", "제주시", "서귀포시"}
    };

    for (String[] region : regions) {
      String city = region[0];
      for (int i = 1; i < region.length; i++) {
        String district = region[i];
        crawlCafesByRegion(city, district);
      }
    }
  }

  public void crawlCafesByRegion(String city, String district) {
    String keyword = city + " " + district + " 키즈카페";
    Map<String, Object> searchResult = naverApiService.searchLocal(keyword);

    Object itemsObject = searchResult.get("items");
    if (!(itemsObject instanceof List<?>)) {
      throw new IllegalArgumentException("네이버 API의 items 형식이 올바르지 않습니다.");
    }

    @SuppressWarnings("unchecked")
    List<Map<String, Object>> cafes = (List<Map<String, Object>>) itemsObject;

    List<Cafe> cafeEntities = new ArrayList<>();
    for (Map<String, Object> cafeData : cafes) {
      Cafe cafe = mapToCafeEntity(cafeData, city + " " + district);
      cafeEntities.add(cafe);
    }

    cafeRepository.saveAll(cafeEntities);
  }

  public List<CafeResponseDto> getAllCafes() {
    List<Cafe> cafes = cafeRepository.findAll();
    List<CafeResponseDto> responseDtos = new ArrayList<>();
    for (Cafe cafe : cafes) {
      responseDtos.add(mapToResponseDto(cafe));
    }
    return responseDtos;
  }

  public CafeResponseDto getCafeDetails(Long cafeId) {
    Cafe cafe = cafeRepository.findById(cafeId)
        .orElseThrow(() -> new IllegalArgumentException("해당 카페를 찾을 수 없습니다."));
    return mapToResponseDto(cafe);
  }

  private Cafe mapToCafeEntity(Map<String, Object> cafeData, String region) {
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
        .region(region)
        .address(address)
        .location(location)
        .size(500)
        .dayOff("주말")
        .parking(true)
        .restaurant(false)
        .careService(false)
        .swimmingPool(false)
        .clothesRental(false)
        .monitoring(false)
        .feedingRoom(false)
        .outdoorPlayground(false)
        .safetyGuard(false)
        .hyperlink((String) cafeData.get("link"))
        .openedAt(java.time.LocalTime.of(9, 0))
        .closedAt(java.time.LocalTime.of(21, 0))
        .build();
  }

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
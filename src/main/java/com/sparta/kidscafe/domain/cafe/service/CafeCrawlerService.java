package com.sparta.kidscafe.domain.cafe.service;

import com.sparta.kidscafe.api.kakao.MapService;
import com.sparta.kidscafe.api.naver.NaverApiResponse;
import com.sparta.kidscafe.api.naver.NaverApiResponse.Item;
import com.sparta.kidscafe.api.naver.NaverApiService;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CafeCrawlerService {

  private static final Logger logger = LoggerFactory.getLogger(CafeCrawlerService.class);
  private final NaverApiService naverApiService;
  private final CafeRepository cafeRepository;
  private final MapService mapService;

  public void crawlingCafe() {
    String[][] regions = mapService.getRegions();
    for (String[] region : regions) {
      int idx = 0;
      String city = region[idx];
      for (idx = 1; idx < region.length; idx++) {
        String district = region[idx];
        crawlCafesByRegion(city, district);
      }
    }
  }

  public void crawlCafesByRegion(String city, String district) {
    try {
      Thread.sleep(1000);
      String region = city + " " + district;
      String keyword = region + " 키즈카페";
      NaverApiResponse response = naverApiService.searchLocal(keyword);
      if (response.getItems() == null || response.getItems().isEmpty()) {
        return;
      }
      cafeRepository.saveAll(convertDtoToEntity(response, region));
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  private List<Cafe> convertDtoToEntity(NaverApiResponse response, String region) {
    List<Cafe> cafes = new ArrayList<>();
    for (Item item : response.getItems()) {
      Point location = mapService.convertAddressToGeo(item.getAddress());
      cafes.add(item.mapToCafeEntity(region, location));
    }
    return cafes;
  }
}
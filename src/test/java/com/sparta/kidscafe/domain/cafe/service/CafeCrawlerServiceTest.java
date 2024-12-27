package com.sparta.kidscafe.domain.cafe.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.kidscafe.api.kakao.MapService;
import com.sparta.kidscafe.api.kakao.Regions;
import com.sparta.kidscafe.api.naver.NaverApiResponse;
import com.sparta.kidscafe.api.naver.NaverApiResponse.Item;
import com.sparta.kidscafe.api.naver.NaverApiService;
import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.domain.user.entity.User;
import com.sparta.kidscafe.dummy.DummyCafe;
import com.sparta.kidscafe.dummy.DummyUser;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CafeCrawlerServiceTest {

  @InjectMocks
  private CafeCrawlerService cafeCrawlerService;

  @Mock
  private NaverApiService naverApiService;

  @Mock
  private CafeRepository cafeRepository;

  @Mock
  private MapService mapService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  private String[][] getRegions() {
    Regions region = new Regions();
    return region.getRegions();
  }

  private NaverApiResponse getNaverApiResponse() {
    NaverApiResponse naverApiResponse = new NaverApiResponse();
    naverApiResponse.setItems(getNaverApiResponseItems());
    return naverApiResponse;
  }

  private List<Item> getNaverApiResponseItems() {
    return List.of(
        new Item("카페A", "http://..", "", "", "", "대한민국 어딘가", "대한민국 어딘가"),
        new Item("카페B", "http://..", "", "", "", "대한민국 어딘가", "대한민국 어딘가")
    );
  }

  @Test
  @DisplayName("카페 크롤링 - 관리자")
  void cafeCrawler_Success() {
    // given
    String[][] regions = {{"Seoul", "Gangnam"}};
    String region = "Seoul" + " Gangnam";
    String keyword = region + " 키즈카페";
    NaverApiResponse response = getNaverApiResponse();
    User user = DummyUser.createDummyUser(RoleType.ADMIN);
    List<Cafe> cafes = DummyCafe.createDummyCafes(user, 1);

    when(naverApiService.searchLocal(keyword)).thenReturn(response);
    when(mapService.getRegions()).thenReturn(regions);
    when(cafeRepository.saveAll(cafes)).thenReturn(cafes);

    // when
    cafeCrawlerService.crawlingCafe();

    // then
    verify(mapService, times(1)).getRegions();
    verify(cafeRepository, times(1)).saveAll(any());
  }
}

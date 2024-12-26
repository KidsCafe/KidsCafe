/*
package com.sparta.kidscafe.api.cafe.runner;

import com.sparta.kidscafe.api.cafe.service.CafeCrawlerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CafeCrawlerRunner implements CommandLineRunner {

  private final CafeCrawlerService cafeCrawlerService;

  public CafeCrawlerRunner(CafeCrawlerService cafeCrawlerService) {
    this.cafeCrawlerService = cafeCrawlerService;
  }

  @Override
  public void run(String... args) throws Exception {
    System.out.println("크롤링 작업을 시작합니다...");

    // 전국 단위 크롤링 실행
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

        System.out.println(city + " " + district + " 크롤링 시작...");
        cafeCrawlerService.crawlCafesByRegion(city, district);

        // 1초 대기
        Thread.sleep(1000);

        System.out.println(city + " " + district + " 크롤링 완료!");
      }
    }

    System.out.println("크롤링 작업이 완료되었습니다!");
  }
}
*/

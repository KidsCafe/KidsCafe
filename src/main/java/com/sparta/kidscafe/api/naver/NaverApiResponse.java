package com.sparta.kidscafe.api.naver;

import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NaverApiResponse {

  private String lastBuildDate; // 응답 생성 날짜
  private int total;           // 총 검색 결과 수
  private int start;           // 검색 시작 위치
  private int display;         // 한 번에 표시할 검색 결과 수
  private List<Item> items;    // 검색 결과 목록

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Item {
    private String title;        // 검색 결과 제목
    private String link;         // 상세 페이지 링크
    private String category;     // 카테고리
    private String description;  // 설명
    private String telephone;    // 전화번호
    private String address;      // 지번 주소
    private String roadAddress;  // 도로명 주소

    public Cafe mapToCafeEntity(String region, Point location) {
      return Cafe.builder()
          .user(null)
          .name(getTitle().replaceAll("<[^>]*>", "")) // HTML 태그 제거
          .hyperlink(getLink())
          .address(getAddress())
          .location(location)
          .region(region)
          .openedAt(null) // 기본 오픈 시간
          .closedAt(null) // 기본 닫는 시간
          .size(0) // 기본 크기 설정
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
    }
  }
}

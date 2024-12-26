package com.sparta.kidscafe.api.naver.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private String mapx;         // x 좌표 (지도용)
    private String mapy;         // y 좌표 (지도용)
  }
}

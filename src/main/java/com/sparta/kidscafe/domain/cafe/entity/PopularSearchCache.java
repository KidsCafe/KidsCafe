//package com.sparta.kidscafe.domain.cafe.entity;
//
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.stream.Collectors;
//
//public class PopularSearchCache {
//
//  private final Map<String, Integer> searchCounts = new ConcurrentHashMap<>();
//
//  // 키워드 추가 및 횟수 증가
//  public void addSearch(String keyword) {
//    searchCounts.merge(keyword, 1, Integer::sum);
//  }
//
//  // 상위 n개의 인기 키워드 조회
//  public List<String> getTopCafes(int n) {
//    return searchCounts.entrySet().stream()
//        .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
//        .limit(10)
//        .map(Map.Entry::getKey)
//        .collect(Collectors.toList());
//  }
//}

//package com.sparta.kidscafe.domain.cafe.controller;
//
//import com.sparta.kidscafe.common.dto.PageResponseDto;
//import com.sparta.kidscafe.common.dto.ResponseDto;
//import com.sparta.kidscafe.domain.cafe.dto.response.CafeSearchResponseDto;
//import com.sparta.kidscafe.domain.cafe.search.service.SearchService;
//import com.sparta.kidscafe.domain.cafe.service.CafeService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api")
//@RequiredArgsConstructor
//public class CafeSearchController {
//
//  private final SearchService cafeSearchService;
//  private final SearchService searchService;
//  private final CafeService cafeService;
//
//  @GetMapping("/search")
//  public ResponseEntity<PageResponseDto<CafeSearchResponseDto>> searchCafesV1(
//      @RequestParam(value = "keyword") String keyword,
//      @RequestParam(value = "page", defaultValue = "0", required = false) int page,
//      @RequestParam(value = "size", defaultValue = "10", required = false) int size
//  ) {
//
//    return ResponseEntity.status(HttpStatus.OK).body(cafeService.searchCafeV1(keyword, page, size));
//  }
//}

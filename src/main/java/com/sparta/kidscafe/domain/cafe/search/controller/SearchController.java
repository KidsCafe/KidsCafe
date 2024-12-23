package com.sparta.kidscafe.domain.cafe.search.controller;

import com.sparta.kidscafe.domain.cafe.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SearchController {

  private final SearchService searchService;

}

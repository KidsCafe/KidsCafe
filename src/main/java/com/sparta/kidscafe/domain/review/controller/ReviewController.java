package com.sparta.kidscafe.domain.review.controller;

import com.sparta.kidscafe.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewController {
  private final ReviewService reviewService;
}

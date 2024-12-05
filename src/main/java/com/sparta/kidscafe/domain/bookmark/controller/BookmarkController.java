package com.sparta.kidscafe.domain.bookmark.controller;

import com.sparta.kidscafe.domain.bookmark.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BookmarkController {

  private final BookmarkService bookmarkService;
}

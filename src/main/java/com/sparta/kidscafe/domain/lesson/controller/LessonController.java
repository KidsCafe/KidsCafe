package com.sparta.kidscafe.domain.lesson.controller;

import com.sparta.kidscafe.domain.lesson.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LessonController {
  private final LessonService lessonService;
}

package com.sparta.kidscafe.domain.lesson.service;

import com.sparta.kidscafe.domain.lesson.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LessonService {
  private final LessonRepository lessonRepository;
}

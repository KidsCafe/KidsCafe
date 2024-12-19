package com.sparta.kidscafe.domain.lesson.service;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.common.util.valid.CafeValidationCheck;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.lesson.dto.request.LessonCreateRequestDto;
import com.sparta.kidscafe.domain.lesson.entity.Lesson;
import com.sparta.kidscafe.domain.lesson.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LessonService {
  private final LessonRepository lessonRepository;
  private final CafeValidationCheck cafeValidationCheck;

  public StatusDto createLesson(AuthUser authUser, Long cafeId, LessonCreateRequestDto requestDto) {
    Cafe cafe = cafeValidationCheck.validMyCafe(cafeId, authUser.getId());
    Lesson lesson = requestDto.convertDtoToEntity(cafe);
    lessonRepository.save(lesson);
    return StatusDto.createStatusDto(
        HttpStatus.CREATED,
        "[" + lesson.getName() + "] 생성 성공"
    );
  }
}

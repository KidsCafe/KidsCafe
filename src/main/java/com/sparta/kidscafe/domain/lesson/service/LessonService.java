package com.sparta.kidscafe.domain.lesson.service;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.util.valid.CafeValidationCheck;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.lesson.dto.request.LessonCreateRequestDto;
import com.sparta.kidscafe.domain.lesson.dto.response.LessonResponseDto;
import com.sparta.kidscafe.domain.lesson.entity.Lesson;
import com.sparta.kidscafe.domain.lesson.repository.LessonRepository;
import com.sparta.kidscafe.exception.BusinessException;
import com.sparta.kidscafe.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LessonService {
  private final LessonRepository lessonRepository;
  private final CafeValidationCheck cafeValidationCheck;

  public void createLesson(AuthUser authUser, Long cafeId, LessonCreateRequestDto requestDto) {
    Cafe cafe = cafeValidationCheck.validMyCafe(cafeId, authUser.getId());
    Lesson lesson = requestDto.convertDtoToEntity(cafe);
    lessonRepository.save(lesson);
  }

  public List<LessonResponseDto> searchLesson(AuthUser authUser, Long cafeId) {
    cafeValidationCheck.validMyCafe(cafeId, authUser.getId());
    return lessonRepository.findAllByCafeId(cafeId)
        .stream()
        .map(LessonResponseDto::from)
        .toList();
  }

  @Transactional
  public void updateLesson(Long lessonId, LessonCreateRequestDto requestDto) {
    Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(
        () -> new BusinessException(ErrorCode.LESSON_NOT_FOUND)
    );
    lesson.update(requestDto);
  }

  public void deleteLesson(Long lessonId) {
    Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(
        () -> new BusinessException(ErrorCode.LESSON_NOT_FOUND)
    );
    lessonRepository.delete(lesson);
  }
}

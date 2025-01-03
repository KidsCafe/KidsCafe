package com.sparta.kidscafe.domain.lesson.controller;

import com.sparta.kidscafe.common.annotation.Auth;
import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.ListResponseDto;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.common.util.valid.AuthValidationCheck;
import com.sparta.kidscafe.domain.lesson.dto.request.LessonCreateRequestDto;
import com.sparta.kidscafe.domain.lesson.dto.response.LessonResponseDto;
import com.sparta.kidscafe.domain.lesson.service.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LessonController {
  private final LessonService lessonService;

  @PostMapping("/cafes/{cafeId}/lessons")
  public ResponseEntity<StatusDto> createLesson(
      @Auth AuthUser authUser,
      @PathVariable Long cafeId,
      @Valid @RequestBody LessonCreateRequestDto requestDto
  ) {
    AuthValidationCheck.validOwner(authUser);
    lessonService.createLesson(authUser, cafeId, requestDto);
    String message = "[" + requestDto.getName() + "] 프로그램 등록 성공";
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(StatusDto.createStatusDto(HttpStatus.CREATED, message));
  }

  @GetMapping("/cafes/{cafeId}/lessons")
  public ResponseEntity<ListResponseDto<LessonResponseDto>> searchLesson(
      @Auth AuthUser authUser,
      @PathVariable Long cafeId
  ) {
    AuthValidationCheck.validOwner(authUser);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(ListResponseDto.create(lessonService.searchLesson(authUser, cafeId)));
  }

  @PatchMapping("/lessons/{lessonId}")
  public ResponseEntity<StatusDto> updateLesson(
      @Auth AuthUser authUser,
      @PathVariable Long lessonId,
      @Valid @RequestBody LessonCreateRequestDto requestDto
  ) {
    AuthValidationCheck.validOwner(authUser);
    lessonService.updateLesson(lessonId, requestDto);
    String message = "[" + requestDto.getName() + "] 수정 성공";
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(StatusDto.createStatusDto(HttpStatus.OK, message));
  }

  @DeleteMapping("lessons/{lessonId}")
  public ResponseEntity<Void> deleteLesson(
      @Auth AuthUser authUser,
      @PathVariable Long lessonId
  ) {
    AuthValidationCheck.validOwner(authUser);
    lessonService.deleteLesson(lessonId);
    return ResponseEntity.noContent().build();
  }
}

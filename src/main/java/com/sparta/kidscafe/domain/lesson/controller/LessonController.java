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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(lessonService.createLesson(authUser, cafeId, requestDto));
  }

  @GetMapping("/cafes/{cafeId}/lessons")
  public ResponseEntity<ListResponseDto<LessonResponseDto>> searchLesson(
      @Auth AuthUser authUser,
      @PathVariable Long cafeId
  ) {
    AuthValidationCheck.validOwner(authUser);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(lessonService.searchLesson(authUser, cafeId));
  }
}

package com.sparta.kidscafe.domain.lesson.dto.response;

import com.sparta.kidscafe.domain.lesson.entity.Lesson;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LessonResponseDto {
  private Long id;
  private String name;
  private String description;
  private int price;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;

  public static LessonResponseDto from(Lesson lesson) {
    return LessonResponseDto.builder()
        .id(lesson.getId())
        .name(lesson.getName())
        .description(lesson.getDescription())
        .price(lesson.getPrice())
        .createdAt(lesson.getCreatedAt())
        .modifiedAt(lesson.getModifiedAt())
        .build();
  }
}

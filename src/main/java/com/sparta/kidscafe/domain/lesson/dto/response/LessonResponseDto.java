package com.sparta.kidscafe.domain.lesson.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonResponseDto {
  private Long id;
  private String name;
  private String description;
  private int price;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}

package com.sparta.kidscafe.domain.lesson.dto.request;

import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.lesson.entity.Lesson;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LessonCreateRequestDto {
  @NotBlank(message = "활동 클래스 이름을 입력해 주세요.")
  private String name;
  private String description;

  @Positive(message = "수업료는 0원 이상입니다.")
  private int price;

  public Lesson convertDtoToEntity(Cafe cafe) {
    return Lesson.builder()
        .cafe(cafe)
        .name(name)
        .description(description)
        .price(price)
        .build();
  }
}

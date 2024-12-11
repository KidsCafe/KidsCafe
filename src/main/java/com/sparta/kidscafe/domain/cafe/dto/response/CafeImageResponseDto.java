package com.sparta.kidscafe.domain.cafe.dto.response;

import com.sparta.kidscafe.domain.cafe.entity.CafeImage;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CafeImageResponseDto {
  private Long id;
  private String imagePath;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;

  public static CafeImageResponseDto from(CafeImage cafeImage) {
    return CafeImageResponseDto.builder()
        .id(cafeImage.getId())
        .imagePath(cafeImage.getImagePath())
        .createdAt(cafeImage.getCreatedAt())
        .modifiedAt(cafeImage.getModifiedAt())
        .build();
  }
}

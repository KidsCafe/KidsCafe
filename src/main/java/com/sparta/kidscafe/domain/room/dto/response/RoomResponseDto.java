package com.sparta.kidscafe.domain.room.dto.response;

import com.sparta.kidscafe.domain.room.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponseDto {
  private Long id;
  private String name;
  private String description;
  private int minCount;
  private int maxCount;
  private int price;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;

  public static RoomResponseDto from(Room room) {
    return RoomResponseDto.builder()
        .id(room.getId())
        .name(room.getName())
        .description(room.getDescription())
        .minCount(room.getMinCount())
        .maxCount(room.getMaxCount())
        .price(room.getPrice())
        .createdAt(room.getCreatedAt())
        .modifiedAt(room.getModifiedAt())
        .build();
  }
}

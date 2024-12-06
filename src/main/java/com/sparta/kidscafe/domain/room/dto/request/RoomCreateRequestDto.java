package com.sparta.kidscafe.domain.room.dto.request;

import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.room.entity.Room;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomCreateRequestDto {

  @NotBlank(message = "방 이름을 입력해 주세요.")
  private String name;
  private String description;

  @Min(value = 1, message = "최소 입장인원은 1명 이상입니다.")
  private int minCount;

  @Min(value = 2, message = "최대 입장인원은 2명 이상입니다.")
  private int maxCount;

  @Positive(message = "방 입장료는 0원 이상입니다.")
  private int price;

  public Room convertDtoToEntity(Cafe cafe) {
    return Room.builder()
        .cafe(cafe)
        .name(name)
        .description(description)
        .minCount(minCount)
        .maxCount(maxCount)
        .price(price)
        .build();
  }
}

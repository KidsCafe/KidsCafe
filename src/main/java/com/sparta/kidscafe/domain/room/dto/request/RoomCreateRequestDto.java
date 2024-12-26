package com.sparta.kidscafe.domain.room.dto.request;

import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.room.entity.Room;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.Map;
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

  @NotNull(message = "최소 입장인원을 입력해 주세요.")
  @Min(value = 1, message = "최소 입장인원은 1명 이상이어야 합니다.")
  private Integer minCount;

  @NotNull(message = "최대 입장인원을 입력해 주세요.")
  @Min(value = 2, message = "최대 입장인원은 2명 이상이어야 합니다.")
  private Integer maxCount;

  @NotNull(message = "입장료를 입력해 주세요.")
  @Positive(message = "방 입장료는 0원 이상이어야 합니다.")
  private Integer price;

  public RoomCreateRequestDto(Map<String, Object> data) {
    this.name = data.get("name").toString();
    this.description = data.getOrDefault("description", "").toString();
    this.minCount = Integer.parseInt(data.getOrDefault("minCount", "1").toString());
    this.maxCount = Integer.parseInt(data.getOrDefault("maxCount", "2").toString());
    this.price = Integer.parseInt(data.getOrDefault("price", "0").toString());
  }

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

package com.sparta.kidscafe.domain.cafe.dto.request;

import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CafeSimpleRequestDto {
  @NotBlank(message = "카페 이름을 입력해주세요.")
  private String name;

  @NotBlank(message = "지역명은 빈칸으로 둘 수 없습니다.")
  private String region;

  @NotBlank(message = "주소를 입력해 주세요.")
  private String address;

  @Positive(message = "카페 크기는 0이상입니다.")
  private int size;

  @Pattern(
      regexp = "^(월|화|수|목|금|토|일)(,\\s*(월|화|수|목|금|토|일))*$",
      message = "DayType은 요일을 쉼표로 구분하여 입력해야 합니다. (예: '월, 화, 수')"
  )
  private String dayOff;

  private boolean multiFamily;
  private boolean parking;
  private boolean restaurant;
  private boolean careService;
  private boolean swimmingPool;
  private boolean clothesRental;
  private boolean monitoring;
  private boolean feedingRoom;
  private boolean outdoorPlayground;
  private boolean safetyGuard;
  private String hyperLink;
  private LocalTime openedAt;
  private LocalTime closedAt;

  public Cafe convertDtoToEntityByCafe(User user) {
    return Cafe.builder()
        .user(user)
        .name(name)
        .region(region)
        .address(address)
        .size(size)
        .dayOff(dayOff)
        .multiFamily(multiFamily)
        .parking(parking)
        .restaurant(restaurant)
        .careService(careService)
        .swimmingPool(swimmingPool)
        .clothesRental(clothesRental)
        .monitoring(monitoring)
        .feedingRoom(feedingRoom)
        .outdoorPlayground(outdoorPlayground)
        .safetyGuard(safetyGuard)
        .hyperlink(hyperLink)
        .openedAt(openedAt)
        .closedAt(closedAt)
        .build();
  }
}

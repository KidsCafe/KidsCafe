package com.sparta.kidscafe.domain.cafe.dto.request;

import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.fee.dto.request.FeeCreateRequestDto;
import com.sparta.kidscafe.domain.fee.entity.Fee;
import com.sparta.kidscafe.domain.lesson.dto.request.LessonCreateRequestDto;
import com.sparta.kidscafe.domain.lesson.entity.Lesson;
import com.sparta.kidscafe.domain.pricepolicy.dto.request.PricePolicyCreateRequestDto;
import com.sparta.kidscafe.domain.pricepolicy.entity.PricePolicy;
import com.sparta.kidscafe.domain.room.dto.request.RoomCreateRequestDto;
import com.sparta.kidscafe.domain.room.entity.Room;
import com.sparta.kidscafe.domain.user.entity.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CafeCreateRequestDto {

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

  @Valid
  private List<Long> images;

  @Valid
  private List<RoomCreateRequestDto> rooms;

  @Valid
  private List<FeeCreateRequestDto> fees;

  @Valid
  private List<LessonCreateRequestDto> lessons;

  @Valid
  private List<PricePolicyCreateRequestDto> pricePolicies;

  public Cafe convertDtoToEntityByCafe(User user, Point location) {
    return Cafe.builder()
        .user(user)
        .name(name)
        .region(region)
        .address(address)
        .location(location)
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

  public List<Room> convertDtoToEntityByRoom(Cafe cafe) {
    List<Room> cafeRooms = new ArrayList<>();
    for(RoomCreateRequestDto dto : rooms)
      cafeRooms.add(dto.convertDtoToEntity(cafe));
    return cafeRooms;
  }

  public List<Lesson> convertDtoToEntityByLesson(Cafe cafe) {
    List<Lesson> cafeLessons = new ArrayList<>();
    for(LessonCreateRequestDto dto : lessons)
      cafeLessons.add(dto.convertDtoToEntity(cafe));
    return cafeLessons;
  }

  public List<Fee> convertDtoToEntityByFee(Cafe cafe) {
    List<Fee> cafeFees = new ArrayList<>();
    for(FeeCreateRequestDto dto : fees)
      cafeFees.add(dto.convertDtoToEntity(cafe));
    return cafeFees;
  }

  public List<PricePolicy> convertDtoToEntityByPricePolicy(Cafe cafe) {
    List<PricePolicy> cafePricePolicies = new ArrayList<>();
    for(PricePolicyCreateRequestDto dto : pricePolicies)
      cafePricePolicies.add(dto.convertDtoToEntity(cafe));
    return cafePricePolicies;
  }
}

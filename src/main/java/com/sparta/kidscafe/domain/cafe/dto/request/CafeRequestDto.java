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
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CafeRequestDto extends CafeSimpleRequestDto {

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
        .name(getName())
        .region(getRegion())
        .address(getAddress())
        .location(location)
        .size(getSize())
        .dayOff(getDayOff())
        .multiFamily(isMultiFamily())
        .parking(isParking())
        .restaurant(isRestaurant())
        .careService(isCareService())
        .swimmingPool(isSwimmingPool())
        .clothesRental(isClothesRental())
        .monitoring(isMonitoring())
        .feedingRoom(isFeedingRoom())
        .outdoorPlayground(isOutdoorPlayground())
        .safetyGuard(isSafetyGuard())
        .hyperlink(getHyperLink())
        .openedAt(getOpenedAt())
        .closedAt(getClosedAt())
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

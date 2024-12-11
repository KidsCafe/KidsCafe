package com.sparta.kidscafe.domain.cafe.dto.request;

import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.fee.dto.request.FeeRequestCreateDto;
import com.sparta.kidscafe.domain.fee.entity.Fee;
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

  private boolean multiFamily;

  @Pattern(
      regexp = "^(월|화|수|목|금|토|일)(,\\s*(월|화|수|목|금|토|일))*$",
      message = "DayType은 요일을 쉼표로 구분하여 입력해야 합니다. (예: '월, 화, 수')"
  )
  private String dayOff;
  private boolean parking;
  private boolean restaurant;
  private String hyperLink;
  private LocalTime openedAt;
  private LocalTime closedAt;

  @Valid
  private List<RoomCreateRequestDto> rooms;

  @Valid
  private List<FeeRequestCreateDto> fees;

  @Valid
  private List<PricePolicyCreateRequestDto> pricePolicies;

  public Cafe convertDtoToEntityByCafe(User user) {
    return Cafe.builder()
        .user(user)
        .name(name)
        .region(region)
        .address(address)
        .size(size)
        .multiFamily(multiFamily)
        .dayOff(dayOff)
        .parking(parking)
        .restaurant(restaurant)
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

  public List<Fee> convertDtoToEntityByFee(Cafe cafe) {
    List<Fee> cafeFees = new ArrayList<>();
    for(FeeRequestCreateDto dto : fees)
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

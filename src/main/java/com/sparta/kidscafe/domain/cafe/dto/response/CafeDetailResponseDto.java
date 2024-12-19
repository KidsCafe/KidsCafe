package com.sparta.kidscafe.domain.cafe.dto.response;

import com.sparta.kidscafe.domain.cafe.entity.CafeImage;
import com.sparta.kidscafe.domain.fee.dto.response.FeeResponseDto;
import com.sparta.kidscafe.domain.fee.entity.Fee;
import com.sparta.kidscafe.domain.lesson.dto.response.LessonResponseDto;
import com.sparta.kidscafe.domain.lesson.entity.Lesson;
import com.sparta.kidscafe.domain.pricepolicy.dto.response.PricePolicyResponseDto;
import com.sparta.kidscafe.domain.pricepolicy.entity.PricePolicy;
import com.sparta.kidscafe.domain.room.dto.response.RoomResponseDto;
import com.sparta.kidscafe.domain.room.entity.Room;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CafeDetailResponseDto {

  private Long id;
  private String name;
  private String address;
  private Double longitude;
  private Double latitude;
  private int size;
  private double star;
  private Long reviewCount;
  private boolean multiFamily;
  private boolean existRoom;
  private String dayOff;
  private boolean parking;
  private boolean existRestaurant;
  private String hyperLink;
  private LocalTime openedAt;
  private LocalTime closedAt;

  private List<CafeImageResponseDto> images;
  private List<RoomResponseDto> rooms;
  private List<LessonResponseDto> lessons;
  private List<FeeResponseDto> fees;
  private List<PricePolicyResponseDto> pricePolicies;

  public void setCafeInfo(CafeResponseDto cafeInfo) {
    id = cafeInfo.getId();
    name = cafeInfo.getName();
    address = cafeInfo.getAddress();
    longitude = cafeInfo.getLongitude();
    latitude = cafeInfo.getLatitude();
    size = cafeInfo.getSize();
    star = cafeInfo.getStar();
    reviewCount = cafeInfo.getReviewCount();
    multiFamily = cafeInfo.isMultiFamily();
    existRoom = cafeInfo.isExistRoom();
    dayOff = cafeInfo.getDayOff();
    parking = cafeInfo.isParking();
    existRestaurant = cafeInfo.isExistRestaurant();
    hyperLink = cafeInfo.getHyperLink();
    openedAt = cafeInfo.getOpenedAt();
    closedAt = cafeInfo.getClosedAt();
  }

  public void setCafeImage(List<CafeImage> images) {
    this.images = new ArrayList<>();
    for (CafeImage image : images) {
      this.images.add(CafeImageResponseDto.from(image));
    }
  }

  public void setRooms(List<Room> rooms) {
    this.rooms = new ArrayList<>();
    for (Room room : rooms) {
      this.rooms.add(RoomResponseDto.from(room));
    }
  }

  public void setLessons(List<Lesson> lessons) {
    this.lessons = new ArrayList<>();
    for (Lesson lesson : lessons) {
      this.lessons.add(LessonResponseDto.from(lesson));
    }
  }

  public void setFees(List<Fee> fees) {
    this.fees = new ArrayList<>();
    for (Fee fee : fees) {
      this.fees.add(FeeResponseDto.from(fee));
    }
  }

  public void setPricePolicies(List<PricePolicy> pricePolicies) {
    this.pricePolicies = new ArrayList<>();
    for (PricePolicy pricePolicy : pricePolicies) {
      this.pricePolicies.add(PricePolicyResponseDto.from(pricePolicy));
    }
  }
}

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
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CafeDetailResponseDto extends CafeResponseDto {

  private List<CafeImageResponseDto> images;
  private List<RoomResponseDto> rooms;
  private List<LessonResponseDto> lessons;
  private List<FeeResponseDto> fees;
  private List<PricePolicyResponseDto> pricePolicies;

  public void setCafeInfo(CafeResponseDto cafeInfo) {
    setId(cafeInfo.getId());
    setName(cafeInfo.getName());
    setAddress(cafeInfo.getAddress());
    setLongitude(cafeInfo.getLongitude());
    setLatitude(cafeInfo.getLatitude());
    setSize(cafeInfo.getSize());
    setStar(cafeInfo.getStar());
    setReviewCount(cafeInfo.getReviewCount());
    setDayOff(cafeInfo.getDayOff());
    setMultiFamily(cafeInfo.isMultiFamily());
    setParking(cafeInfo.isParking());
    setExistRestaurant(cafeInfo.isExistRestaurant());
    setExistCareService(cafeInfo.isExistCareService());
    setExistSwimmingPool(cafeInfo.isExistSwimmingPool());
    setExistClothesRental(cafeInfo.isExistClothesRental());
    setExistMonitoring(cafeInfo.isExistMonitoring());
    setExistOutdoorPlayground(cafeInfo.isExistOutdoorPlayground());
    setExistSafetyGuard(cafeInfo.isExistSafetyGuard());
    setExistRoom(cafeInfo.isExistRoom());
    setExistLesson(cafeInfo.isExistLesson());
    setHyperLink(cafeInfo.getHyperLink());
    setOpenedAt(cafeInfo.getOpenedAt());
    setClosedAt(cafeInfo.getClosedAt());
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

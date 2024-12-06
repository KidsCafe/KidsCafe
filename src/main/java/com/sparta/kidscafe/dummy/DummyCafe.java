package com.sparta.kidscafe.dummy;

import com.sparta.kidscafe.common.util.TestUtil;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.user.entity.User;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DummyCafe {

  public static Cafe createDummyCafe(User owner) {
    String randomCafeName = TestUtil.getRandomString(10);
    String region = "부산광역시";
    String address = "대한민국 어딘가";
    int randomSize = TestUtil.getRandomInteger(50, 500);
    boolean randomMultiFamily = TestUtil.getRandomBoolean();
    boolean randomRoomExist = TestUtil.getRandomBoolean();
    String randomDayOff = TestUtil.getRandomDayOff();
    boolean randomParking = TestUtil.getRandomBoolean();
    boolean randomRestaurant = TestUtil.getRandomBoolean();
    String randomHyperLink = "http://..." + TestUtil.getRandomString(10);
    LocalTime randomOpenedAt = TestUtil.getRandomLocalTime(5, 12);
    LocalTime randomClosedAt = TestUtil.getRandomLocalTime(6, 24);
    return Cafe.builder()
        .user(owner)
        .name(randomCafeName)
        .region(region)
        .address(address)
        .size(randomSize)
        .multiFamily(randomMultiFamily)
        .roomExist(randomRoomExist)
        .dayOff(randomDayOff)
        .parking(randomParking)
        .restaurant(randomRestaurant)
        .hyperlink(randomHyperLink)
        .openedAt(randomOpenedAt)
        .closedAt(randomClosedAt)
        .build();
  }

  public static List<Cafe> createDummyCafes(User owner, int size) {
    List<Cafe> cafes = new ArrayList<>();
    for(int idx = 0; idx < size; idx++)
      cafes.add(createDummyCafe(owner));
    return cafes;
  }
}

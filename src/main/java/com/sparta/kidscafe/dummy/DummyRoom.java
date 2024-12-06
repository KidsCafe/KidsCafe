package com.sparta.kidscafe.dummy;

import com.sparta.kidscafe.common.util.TestUtil;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.room.entity.Room;
import java.util.ArrayList;
import java.util.List;

public class DummyRoom {
  public static Room createDummyRoom(Cafe cafe) {
    String randomName = TestUtil.getRandomString(15);
    String randomDescription = TestUtil.getRandomString(30);
    int randomMinCount = TestUtil.getRandomInteger(1, 4);
    int randomMaxCount = TestUtil.getRandomInteger(randomMinCount, randomMinCount + 10);
    int randomPrice = TestUtil.getRandomPrice(8000, 30000);
    return Room.builder()
        .cafe(cafe)
        .name(randomName)
        .description(randomDescription)
        .minCount(randomMinCount)
        .maxCount(randomMaxCount)
        .price(randomPrice)
        .build();
  }

  public static List<Room> createDummyRooms(Cafe cafe, int size) {
    List<Room> rooms = new ArrayList<>();
    for(int idx = 0; idx < size; idx++)
      rooms.add(createDummyRoom(cafe));
    return rooms;
  }
}

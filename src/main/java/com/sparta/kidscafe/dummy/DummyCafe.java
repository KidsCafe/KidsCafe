package com.sparta.kidscafe.dummy;

import com.sparta.kidscafe.common.util.GeoUtil;
import com.sparta.kidscafe.common.util.TestUtil;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.user.entity.User;
import org.locationtech.jts.geom.Point;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DummyCafe {

  public static Cafe createDummyCafe(User owner, Long cafeId) {
    GeoUtil geoUtil = new GeoUtil();
    String randomCafeName = TestUtil.getRandomString(10);
    String region = TestUtil.getRandomRegion();
    String address = "대한민국 어딘가";
    Double randomLon = TestUtil.getRandomDouble(124.6, 131.9);
    Double randomLat = TestUtil.getRandomDouble(33.1, 38.6);
    Point randomLocation = geoUtil.convertGeoToPoint(randomLon, randomLat);
    int randomSize = TestUtil.getRandomInteger(50, 500);
    boolean randomMultiFamily = TestUtil.getRandomBoolean();
    String randomDayOff = TestUtil.getRandomDayOff();
    boolean randomParking = TestUtil.getRandomBoolean();
    boolean randomRestaurant = TestUtil.getRandomBoolean();
    String randomHyperLink = "http://..." + TestUtil.getRandomString(10);
    LocalTime randomOpenedAt = TestUtil.getRandomLocalTime(5, 12);
    LocalTime randomClosedAt = TestUtil.getRandomLocalTime(6, 24);
    return Cafe.builder()
        .id(cafeId)
        .user(owner)
        .name(randomCafeName)
        .region(region)
        .address(address)
        .location(randomLocation)
        .size(randomSize)
        .multiFamily(randomMultiFamily)
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
    for (int idx = 0; idx < size; idx++) {
      cafes.add(createDummyCafe(owner, null));
    }
    return cafes;
  }
}

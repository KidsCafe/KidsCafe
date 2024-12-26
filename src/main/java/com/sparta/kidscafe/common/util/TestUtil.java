package com.sparta.kidscafe.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.apache.commons.lang3.RandomStringUtils;

public class TestUtil {
  public static int getRandomInteger(int min, int max) {
    int range = max - min + 1;
    return (int)(Math.random() * range) + min;
  }

  public static double getRandomDouble(int min, int max) {
    int range = max - min;
    double randomValue = Math.random() * range;
    return Math.round(randomValue * 10) / 10.0;
  }

  public static double getRandomDouble(double min, double max) {
    double range = max - min;
    return Math.random() * range;
  }

  public static int getRandomPrice(int min, int max) {
    int range = (max/1000) - (min/1000) + 1;
    int randomPrice = (int)(Math.random() * range) + (min/1000);
    return randomPrice * 1000;
  }

  public static boolean getRandomBoolean() {
    Random random = new Random();
    return random.nextBoolean();
  }

  public static String getRandomString(int length) {
    return RandomStringUtils.randomAlphanumeric(length);
  }

  public static String getRandomDayOff() {
    List<String> week = new ArrayList<>();
    week.add("월");
    week.add("화");
    week.add("수");
    week.add("목");
    week.add("금");
    week.add("토");
    week.add("일");
    Collections.shuffle(week);

    int cntDayOff = getRandomInteger(1, 7);
    StringBuilder sb = new StringBuilder();
    for(int idx = 0; idx < cntDayOff; idx++) {
      sb.append(week.get(idx));
      if(idx != cntDayOff - 1) {
        sb.append(",");
      }
    }
    return sb.toString();
  }

  public static LocalTime getRandomLocalTime(int minHour, int maxHour) {
    Random random = new Random();
    int hour = random.nextInt(getRandomInteger(minHour, maxHour));
    int minute = random.nextInt(6) * 10; // 0, 10, 20, 30, 40, 50
    return LocalTime.of(hour, minute);
  }

  public static LocalDateTime getRandomLocalDateTime(int minHour, int maxHour) {
    Random random = new Random();
    int hour = random.nextInt(getRandomInteger(minHour, maxHour));
    int minute = random.nextInt(6) * 10; // 0, 10, 20, 30, 40, 50
    return LocalDateTime.of(LocalDate.now(), LocalTime.of(hour, minute));
  }
}

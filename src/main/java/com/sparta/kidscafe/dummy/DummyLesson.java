package com.sparta.kidscafe.dummy;

import com.sparta.kidscafe.common.util.TestUtil;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.lesson.entity.Lesson;

import java.util.ArrayList;
import java.util.List;

public class DummyLesson {

  public static Lesson createDummyLesson(Cafe cafe, Long id) {
    String randomName = TestUtil.getRandomString(15);
    String randomDescription = TestUtil.getRandomString(30);
    int randomPrice = TestUtil.getRandomPrice(3000, 30000);
    return Lesson.builder()
        .id(id)
        .cafe(cafe)
        .name(randomName)
        .description(randomDescription)
        .price(randomPrice)
        .build();
  }

  public static Lesson createDummyLesson(Cafe cafe) {
    String randomName = TestUtil.getRandomString(15);
    String randomDescription = TestUtil.getRandomString(30);
    int randomPrice = TestUtil.getRandomPrice(3000, 30000);
    return Lesson.builder()
        .cafe(cafe)
        .name(randomName)
        .description(randomDescription)
        .price(randomPrice)
        .build();
  }

  public static List<Lesson> createDummyLessons(Cafe cafe, int size) {
    List<Lesson> lessons = new ArrayList<>();
    for (int idx = 0; idx < size; idx++) {
      lessons.add(createDummyLesson(cafe));
    }
    return lessons;
  }
}

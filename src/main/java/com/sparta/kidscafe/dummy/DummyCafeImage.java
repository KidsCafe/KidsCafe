package com.sparta.kidscafe.dummy;

import com.sparta.kidscafe.common.util.TestUtil;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.entity.CafeImage;

import java.util.ArrayList;
import java.util.List;

public class DummyCafeImage {

  public static CafeImage createDummyCafeImage(Cafe cafe, String path) {
    return CafeImage.builder()
        .cafe(cafe) // 수정: cafe 객체를 직접 설정
        .imagePath(path)
        .build();
  }

  public static CafeImage createDummyCafeImage(Long id, Cafe cafe) {
    return CafeImage.builder()
        .id(id)
        .cafe(cafe) // 수정: cafe 객체를 직접 설정
        .build();
  }

  public static CafeImage createDummyCafeImage(Cafe cafe) {
    String randomImagePath = "https://..." + TestUtil.getRandomString(10) + ".jpg";
    return CafeImage.builder()
        .cafe(cafe) // 수정: cafe 객체를 직접 설정
        .imagePath(randomImagePath)
        .build();
  }

  public static List<CafeImage> createDummyCafeImages(Cafe cafe, int size) {
    List<CafeImage> images = new ArrayList<>();
    for (int idx = 0; idx < size; idx++) {
      images.add(createDummyCafeImage(cafe));
    }
    return images;
  }

  public static List<CafeImage> createDummyCafeImages(Cafe cafe, List<Long> ids) {
    List<CafeImage> images = new ArrayList<>();
    for (Long id : ids) {
      images.add(createDummyCafeImage(id, cafe));
    }
    return images;
  }

  public static List<CafeImage> createDummyGhostImages(int size) {
    String randomImagePath = "https://..." + TestUtil.getRandomString(10) + ".jpg";
    List<CafeImage> images = new ArrayList<>();
    for (int idx = 0; idx < size; idx++) {
      images.add(CafeImage.builder().imagePath(randomImagePath).build());
    }
    return images;
  }
}

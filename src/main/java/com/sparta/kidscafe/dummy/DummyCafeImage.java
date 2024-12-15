package com.sparta.kidscafe.dummy;

import com.sparta.kidscafe.common.util.TestUtil;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.entity.CafeImage;
import java.util.ArrayList;
import java.util.List;

public class DummyCafeImage {

  public static CafeImage createDummyCafeImage(Cafe cafe) {
    String randomImagePath = "http://..." + TestUtil.getRandomString(10) + ".jpg";
    return CafeImage.builder()
        .cafeId(cafe.getId())
        .imagePath(randomImagePath)
        .build();
  }

  public static List<CafeImage> createDummyCafeImages(Cafe cafe, int size) {
    List<CafeImage> images = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      images.add(createDummyCafeImage(cafe));
    }
    return images;
  }
}

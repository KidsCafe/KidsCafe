package com.sparta.kidscafe.domain.cafe.view.dummy;

import com.sparta.kidscafe.common.util.TestUtil;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.entity.CafeViewCount;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.domain.cafe.repository.CafeViewCountRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@Tag("dummy-test")
@SpringBootTest
public class CafeViewCountDummyTest {

  @Autowired
  private CafeRepository cafeRepository;

  @Autowired
  private CafeViewCountRepository cafeViewCountRepository;

  @Test
  @Rollback(false)
  void createCafeViewCountDummyData() {
    List<Cafe> cafes = cafeRepository.findAll();

    cafeViewCountRepository.deleteAll();

    List<CafeViewCount> dummyData = new ArrayList<>();
    for (Cafe cafe : cafes) {
      int randomViewCount = TestUtil.getRandomInteger(0,1000);
      CafeViewCount cafeViewCount = CafeViewCount.builder()
          .cafe(cafe)
          .viewCount(randomViewCount)
          .build();
      dummyData.add(cafeViewCount);
    }
    cafeViewCountRepository.saveAll(dummyData);
    System.out.println("총 " + dummyData.size() + "개의 CafeViewCount 더미 데이터를 생성했습니다.");
  }

}

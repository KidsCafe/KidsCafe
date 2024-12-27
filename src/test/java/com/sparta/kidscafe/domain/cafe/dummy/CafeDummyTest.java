package com.sparta.kidscafe.domain.cafe.dummy;

import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.common.util.TestUtil;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.entity.CafeImage;
import com.sparta.kidscafe.domain.cafe.repository.CafeImageRepository;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.domain.fee.entity.Fee;
import com.sparta.kidscafe.domain.fee.repository.FeeRepository;
import com.sparta.kidscafe.domain.lesson.entity.Lesson;
import com.sparta.kidscafe.domain.lesson.repository.LessonRepository;
import com.sparta.kidscafe.domain.pricepolicy.entity.PricePolicy;
import com.sparta.kidscafe.domain.pricepolicy.repository.PricePolicyRepository;
import com.sparta.kidscafe.domain.room.entity.Room;
import com.sparta.kidscafe.domain.room.repository.RoomRepository;
import com.sparta.kidscafe.domain.user.entity.User;
import com.sparta.kidscafe.domain.user.repository.UserRepository;
import com.sparta.kidscafe.dummy.DummyCafe;
import com.sparta.kidscafe.dummy.DummyCafeImage;
import com.sparta.kidscafe.dummy.DummyFee;
import com.sparta.kidscafe.dummy.DummyLesson;
import com.sparta.kidscafe.dummy.DummyPricePolicy;
import com.sparta.kidscafe.dummy.DummyRoom;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("!test")
@SpringBootTest
public class CafeDummyTest {

  @Autowired
  private CafeRepository cafeRepository;

  @Autowired
  private CafeImageRepository cafeImageRepository;

  @Autowired
  private RoomRepository roomRepository;

  @Autowired
  private FeeRepository feeRepository;

  @Autowired
  private LessonRepository lessonRepository;

  @Autowired
  private PricePolicyRepository pricePolicyRepository;

  @Autowired
  private UserRepository userRepository;

  @Test
  @Transactional
  @Rollback(false)
  void createCafe() {
    //  user dummy test 돌려야함
    List<User> owners = userRepository.findAllByRole(RoleType.OWNER);
    for (User owner : owners) {
      List<Cafe> cafes = DummyCafe.createDummyCafes(owner, 10);
      cafeRepository.saveAll(cafes);

      for (Cafe cafe : cafes) {
        List<CafeImage> cafeImages = DummyCafeImage.createDummyCafeImages(cafe, 5);
        cafeImageRepository.saveAll(cafeImages);

        List<Room> rooms = DummyRoom.createDummyRooms(cafe, TestUtil.getRandomInteger(0, 5));
        roomRepository.saveAll(rooms);

        List<Lesson> lessons = DummyLesson.createDummyLessons(cafe, TestUtil.getRandomInteger(0, 5));
        lessonRepository.saveAll(lessons);

        List<Fee> fees = DummyFee.createDummyFees(cafe);
        feeRepository.saveAll(fees);

        Collections.shuffle(rooms);
        Collections.shuffle(fees);

        List<PricePolicy> pricePolicies = DummyPricePolicy
            .createDummyPricePoliciesByFee(
                cafe,
                fees.subList(0, TestUtil.getRandomInteger(1, fees.size())));
        if (!rooms.isEmpty()) {
          pricePolicies.addAll(
              DummyPricePolicy
                  .createDummyPricePoliciesByRoom(
                      cafe,
                      rooms.subList(0, TestUtil.getRandomInteger(1, rooms.size()))));
        }

        pricePolicyRepository.saveAll(pricePolicies);
      }
    }
  }
}

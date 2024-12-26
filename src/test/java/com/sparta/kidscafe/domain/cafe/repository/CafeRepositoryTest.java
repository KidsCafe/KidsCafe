package com.sparta.kidscafe.domain.cafe.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sparta.kidscafe.common.enums.AgeGroup;
import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.domain.cafe.dto.response.CafeResponseDto;
import com.sparta.kidscafe.domain.cafe.dto.response.CafeSimpleResponseDto;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.repository.condition.CafeSearchCondition;
import com.sparta.kidscafe.domain.cafe.repository.sort.CafeSearchSortBy;
import com.sparta.kidscafe.domain.user.entity.User;
import com.sparta.kidscafe.domain.user.repository.UserRepository;
import com.sparta.kidscafe.dummy.DummyUser;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class CafeRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CafeRepository cafeRepository;

  @Autowired
  private GeometryFactory geometryFactory;

  private Cafe cafe;
  private CafeSearchCondition cafeSearchCondition;

  @BeforeEach
  void setUp() {
    cafe = createCafe();
    cafeSearchCondition = createCafeCondition();
  }

  CafeSearchCondition createCafeCondition() {
    return CafeSearchCondition.createBuilder()
        .name("키즈")
        .region("서울특별시")
        .size(50000)
        .ageGroup(AgeGroup.BABY.toString())
        .minPrice(10)
        .maxPrice(10000)
        .minStar(0.0)
        .maxStar(5.0)
        .parking(true)
        .opening(null)
        .multiFamily(true)
        .existRestaurant(true)
        .existCareService(true)
        .existClothesRental(true)
        .existMonitoring(true)
        .existFeedingRoom(true)
        .existOutdoorPlayground(true)
        .existSafetyGuard(true)
        .existRoom(false)
        .existLesson(false)
        .adultPrice(null)
        .openedAt(null)
        .closedAt(null)
        .lon(127.0)
        .lat(38.0)
        .radiusMeter(100000.0)
        .pageable(PageRequest.of(0, 10))
        .sortBy(CafeSearchSortBy.CAFE_NAME)
        .asc(true)
        .build();
  }

  Cafe createCafe() {
    User user = DummyUser.createDummyUser(RoleType.OWNER);
    userRepository.save(user);

    Point location = geometryFactory.createPoint(
        new Coordinate(127.0585726882262, 37.66015485, 0.0));
    location.setSRID(4326);
    Cafe cafe = Cafe.builder()
        .user(user)
        .name("평범한 키즈 카페")
        .region("서울특별시")
        .address("서울특별시 어딘가")
        .location(location)
        .multiFamily(true)
        .dayOff("월 수 금")
        .parking(true)
        .restaurant(true)
        .careService(true)
        .swimmingPool(true)
        .clothesRental(true)
        .monitoring(true)
        .feedingRoom(true)
        .outdoorPlayground(true)
        .safetyGuard(true)
        .hyperlink("http://...")
        .openedAt(LocalTime.of(9, 0))
        .closedAt(LocalTime.of(22, 0))
        .build();
    cafeRepository.save(cafe);
    return cafe;
  }

  @Test
  @DisplayName("카페 상세 조회 - 성공")
  void findCafeById() {
    // given & when
    CafeResponseDto result = cafeRepository.findCafeById(cafe.getId());

    // then
    assertThat(result.getName()).isEqualTo(cafe.getName());
    assertThat(result).isNotNull();
  }

  @Test
  @DisplayName("카페 목록 조회 - 성공")
  void findAllByCafeSimple() {
    // given & when
    Page<CafeSimpleResponseDto> result = cafeRepository.findAllByCafeSimple(cafeSearchCondition);

    // then
    assertThat(result).isNotEmpty();
    assertThat(result.getContent().size()).isGreaterThan(0);
  }

  @Test
  @DisplayName("카페 총 갯수 조회 - 성공")
  void searchTotalCount() {
    // given & when
    Long totalCount = cafeRepository.searchTotalCount(cafeSearchCondition);

    // then
    assertThat(totalCount).isEqualTo(1L);
  }
}

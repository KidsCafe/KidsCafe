package com.sparta.kidscafe.domain.cafe.service;

import com.sparta.kidscafe.api.kakao.MapService;
import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.common.util.valid.CafeValidationCheck;
import com.sparta.kidscafe.common.util.valid.UserValidationCheck;
import com.sparta.kidscafe.domain.cafe.dto.request.CafeRequestDto;
import com.sparta.kidscafe.domain.cafe.dto.request.CafeSimpleRequestDto;
import com.sparta.kidscafe.domain.cafe.dto.response.CafeDetailResponseDto;
import com.sparta.kidscafe.domain.cafe.dto.response.CafeResponseDto;
import com.sparta.kidscafe.domain.cafe.dto.response.CafeSimpleResponseDto;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.entity.CafeImage;
import com.sparta.kidscafe.domain.cafe.repository.CafeImageRepository;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.domain.cafe.repository.condition.CafeSearchCondition;
import com.sparta.kidscafe.domain.fee.entity.Fee;
import com.sparta.kidscafe.domain.lesson.entity.Lesson;
import com.sparta.kidscafe.domain.pricepolicy.entity.PricePolicy;
import com.sparta.kidscafe.domain.room.entity.Room;
import com.sparta.kidscafe.domain.user.entity.User;
import com.sparta.kidscafe.dummy.DummyCafe;
import com.sparta.kidscafe.dummy.DummyCafeImage;
import com.sparta.kidscafe.dummy.DummyUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class CafeServiceTest {

  @InjectMocks
  private CafeService cafeService;

  @Mock
  private CafeRepository cafeRepository;

  @Mock
  private CafeImageRepository cafeImageRepository;

  @Mock
  private CafeRequestDto requestCafe;

  @Mock
  private CafeSimpleRequestDto requestSimpleCafe;

  @Mock
  private UserValidationCheck userValidationCheck;

  @Mock
  private CafeValidationCheck cafeValidationCheck;

  @Mock
  private MapService mapService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  private AuthUser createAuthUser(RoleType role) {
    return new AuthUser(1L, "hong@email.com", role);
  }

  private CafeSimpleRequestDto createCafeSimpleRequestDto() {
    return CafeSimpleRequestDto.builder().build();
  }

  private CafeResponseDto createResponseDto(Long id) {
    return CafeResponseDto.createBuilder()
        .id(id)
        .name("Test Cafe")
        .build();
  }

  @Test
  @DisplayName("카페 등록 성공 - 사장님")
  void createCafeOwner_Success() {
    // given
    AuthUser authUser = createAuthUser(RoleType.OWNER);
    User user = DummyUser.createDummyUser(authUser.getRoleType());
    Cafe cafe = DummyCafe.createDummyCafe(user, null);

    when(requestCafe.convertDtoToEntity(user, null)).thenReturn(cafe);
    when(requestCafe.convertDtoToEntityByRoom(cafe)).thenReturn(Collections.singletonList(mock(Room.class)));
    when(requestCafe.convertDtoToEntityByLesson(cafe)).thenReturn(Collections.singletonList(mock(Lesson.class)));
    when(requestCafe.convertDtoToEntityByFee(cafe)).thenReturn(Collections.singletonList(mock(Fee.class)));
    when(requestCafe.convertDtoToEntityByPricePolicy(cafe)).thenReturn(Collections.singletonList(mock(PricePolicy.class)));

    when(userValidationCheck.findUser(authUser.getId())).thenReturn(user);
    doNothing().when(cafeValidationCheck).validOverMaximum(authUser.getId());
    when(mapService.convertAddressToGeo(cafe.getAddress())).thenReturn(null);
    when(cafeRepository.save(any(Cafe.class))).thenReturn(cafe);

    // when
    cafeService.createCafe(authUser, requestCafe);

    // then
    verify(userValidationCheck).findUser(authUser.getId());
    verify(cafeValidationCheck).validOverMaximum(authUser.getId());
    verify(cafeRepository).save(any(Cafe.class));
    verify(cafeImageRepository).findAllById(requestCafe.getImages());
  }

  @Test
  @DisplayName("카페 등록 성공 - 관리자")
  void creatCafe_MultipleCafes_Success() {
    // given
    CafeSimpleRequestDto requestCafe1 = createCafeSimpleRequestDto();
    CafeSimpleRequestDto requestCafe2 = createCafeSimpleRequestDto();
    List<CafeSimpleRequestDto> requestCafes = List.of(requestCafe1, requestCafe2);
    AuthUser authUser = createAuthUser(RoleType.ADMIN);
    User user = DummyUser.createDummyUser(authUser.getRoleType());
    List<Cafe> cafes = DummyCafe.createDummyCafes(user, 2);

    when(userValidationCheck.findUser(authUser.getId())).thenReturn(user);
    when(cafeRepository.saveAll(cafes)).thenReturn(cafes);

    // when
    cafeService.creatCafe(authUser, requestCafes);

    // then
    verify(userValidationCheck).findUser(authUser.getId());
    verify(mapService, times(2)).convertAddressToGeo(any());
    verify(cafeRepository).saveAll(any());
  }

  @Test
  @DisplayName("카페 조회 성공")
  void searchCafe_Success() {
    // given
    CafeSearchCondition condition = mock(CafeSearchCondition.class);
    CafeSimpleResponseDto responseCafe1 = new CafeResponseDto();
    CafeSimpleResponseDto responseCafe2 = new CafeResponseDto();
    Page<CafeSimpleResponseDto> cafes = new PageImpl<>(List.of(responseCafe1, responseCafe2));
    when(cafeRepository.findAllByCafeSimple(condition)).thenReturn(cafes);

    // when
    Page<CafeSimpleResponseDto> result = cafeService.searchCafe(condition);

    // than
    assertEquals(2, result.getSize());
    verify(cafeRepository).findAllByCafeSimple(condition);
  }

  @Test
  @DisplayName("카페 상세 조회 성공")
  void findCafe_Success() {
    // given
    Long cafeId = 1L;
    User user = DummyUser.createDummyUser(RoleType.OWNER);
    Cafe cafe = DummyCafe.createDummyCafe(user, cafeId);

    CafeResponseDto cafeResponseDto = createResponseDto(cafeId);
    when(cafeRepository.findCafeById(cafeId)).thenReturn(cafeResponseDto);
    when(cafeValidationCheck.findCafe(cafeId)).thenReturn(cafe);

    // when
    CafeDetailResponseDto result = cafeService.findCafe(cafeId);

    // then
    assertEquals("Test Cafe", result.getName());
    verify(cafeRepository).findCafeById(cafeId);
  }

  @Test
  @DisplayName("카페 상세 조회 - 검색 결과가 없음")
  void findCafe_NotFound() {
    // given
    Long cafeId = 1L;
    when(cafeRepository.findCafeById(cafeId)).thenReturn(null);

    // when
    CafeDetailResponseDto result = cafeService.findCafe(cafeId);

    // then
    assertNull(result);
    verify(cafeRepository).findCafeById(cafeId);
  }

  @Test
  @DisplayName("카페 수정 성공")
  void updateCafe_Success() {
    // given
    Long cafeId = 2L;
    AuthUser authUser = createAuthUser(RoleType.OWNER);
    User user = DummyUser.createDummyUser(authUser.getRoleType());
    Cafe cafe = DummyCafe.createDummyCafe(user, cafeId);

    when(mapService.convertAddressToGeo(cafe.getAddress())).thenReturn(null);
    when(cafeValidationCheck.validMyCafe(cafeId, authUser.getId())).thenReturn(cafe);

    // when
    cafeService.updateCafe(authUser, cafeId, requestSimpleCafe);

    // then
    verify(cafeValidationCheck).validMyCafe(cafeId, authUser.getId());
    verify(mapService).convertAddressToGeo(any());
  }

  @Test
  @DisplayName("카페 삭제 성공 - 사장님")
  void deleteCafe_Success() {
    // given
    Long cafeId = 1L;
    AuthUser authUser = createAuthUser(RoleType.OWNER);
    User user = DummyUser.createDummyUser(authUser.getRoleType());
    Cafe cafe = DummyCafe.createDummyCafe(user, cafeId);
    List<CafeImage> cafeImages = DummyCafeImage.createDummyCafeImages(cafe, 2);

    when(cafeValidationCheck.validMyCafe(cafeId, authUser.getId())).thenReturn(cafe);
    when(cafeImageRepository.findAllByCafeId(cafeId)).thenReturn(cafeImages);

    // when
    cafeService.deleteCafe(authUser, cafeId);

    // then
    verify(cafeValidationCheck).validMyCafe(cafeId, authUser.getId());
    verify(cafeRepository).delete(cafe);
    verify(cafeImageRepository).findAllByCafeId(cafeId);
  }

  @Test
  @DisplayName("카페 삭제 성공 - 관리자")
  void deleteMultipleCafes_Success() {
    // given
    AuthUser authUser = createAuthUser(RoleType.OWNER);
    User user = DummyUser.createDummyUser(authUser.getRoleType());
    List<Long> cafeIds = List.of(1L, 2L);
    List<Cafe> cafes = DummyCafe.createDummyCafes(user, cafeIds.size());
    when(cafeRepository.findAllByUserIdAndIdIn(authUser.getId(), cafeIds)).thenReturn(cafes);

    // when
    cafeService.deleteCafe(authUser, cafeIds);

    // then
    verify(cafeRepository).findAllByUserIdAndIdIn(authUser.getId(), cafeIds);
    verify(cafeRepository).deleteAll(cafes);
  }
}
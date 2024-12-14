package com.sparta.kidscafe.domain.cafe.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.PageResponseDto;
import com.sparta.kidscafe.common.dto.ResponseDto;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.common.util.valid.CafeValidationCheck;
import com.sparta.kidscafe.common.util.valid.UserValidationCheck;
import com.sparta.kidscafe.domain.cafe.dto.request.CafeCreateRequestDto;
import com.sparta.kidscafe.domain.cafe.dto.request.CafeSimpleRequestDto;
import com.sparta.kidscafe.domain.cafe.dto.request.CafesSimpleCreateRequestDto;
import com.sparta.kidscafe.domain.cafe.dto.response.CafeDetailResponseDto;
import com.sparta.kidscafe.domain.cafe.dto.response.CafeResponseDto;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.entity.CafeImage;
import com.sparta.kidscafe.domain.cafe.repository.CafeImageRepository;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.domain.cafe.repository.condition.CafeSearchCondition;
import com.sparta.kidscafe.domain.fee.entity.Fee;
import com.sparta.kidscafe.domain.fee.repository.FeeRepository;
import com.sparta.kidscafe.domain.pricepolicy.entity.PricePolicy;
import com.sparta.kidscafe.domain.pricepolicy.repository.PricePolicyRepository;
import com.sparta.kidscafe.domain.room.entity.Room;
import com.sparta.kidscafe.domain.room.repository.RoomRepository;
import com.sparta.kidscafe.domain.user.entity.User;
import com.sparta.kidscafe.dummy.DummyCafe;
import com.sparta.kidscafe.dummy.DummyCafeImage;
import com.sparta.kidscafe.dummy.DummyUser;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;

class CafeServiceTest {

  @InjectMocks
  private CafeService cafeService;

  @Mock
  private CafeRepository cafeRepository;

  @Mock
  private CafeImageRepository cafeImageRepository;

  @Mock
  private RoomRepository roomRepository;

  @Mock
  private FeeRepository feeRepository;

  @Mock
  private PricePolicyRepository pricePolicyRepository;

  @Mock
  private CafeCreateRequestDto cafeCreateRequestDto;

  @Mock
  private CafesSimpleCreateRequestDto cafesSimpleCreateRequestDto;

  @Mock
  private CafeSimpleRequestDto cafeSimpleRequestDto;

  @Mock
  private UserValidationCheck userValidationCheck;

  @Mock
  private CafeValidationCheck cafeValidationCheck;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  private AuthUser createAuthUser(RoleType role) {
    return new AuthUser(1L, "hong@email.com", role);
  }

  @Test
  @DisplayName("카페 등록 성공 - 사장님")
  void createCafeOwner_Success() {
    // given
    AuthUser authUser = createAuthUser(RoleType.OWNER);
    User user = DummyUser.createDummyUser(authUser.getRoleType());
    Cafe cafe = DummyCafe.createDummyCafe(user, null);

    when(userValidationCheck.validMy(authUser.getId())).thenReturn(user);
    when(cafeCreateRequestDto.convertDtoToEntityByCafe(user)).thenReturn(cafe);
    when(cafeCreateRequestDto.convertDtoToEntityByRoom(cafe)).thenReturn(Collections.singletonList(mock(Room.class)));
    when(cafeCreateRequestDto.convertDtoToEntityByFee(cafe)).thenReturn(Collections.singletonList(mock(Fee.class)));
    when(cafeCreateRequestDto.convertDtoToEntityByPricePolicy(cafe)).thenReturn(Collections.singletonList(mock(PricePolicy.class)));

    when(userValidationCheck.validMy(authUser.getId())).thenReturn(user);
    when(cafeRepository.save(any(Cafe.class))).thenReturn(cafe);

    // when
    StatusDto result = cafeService.createCafe(authUser, cafeCreateRequestDto);

    // then
    assertEquals(HttpStatus.CREATED.value(), result.getStatus());
    assertEquals("[" + cafe.getName() + "] 등록 성공", result.getMessage());

    verify(userValidationCheck).validMy(authUser.getId());
    verify(cafeRepository).save(any(Cafe.class));
    verify(cafeImageRepository).findAllById(cafeCreateRequestDto.getImages());
    verify(roomRepository).saveAll(any());
    verify(feeRepository).saveAll(any());
    verify(pricePolicyRepository).saveAll(any());
  }

  @Test
  @DisplayName("카페 등록 성공 - 관리자")
  void creatCafe_MultipleCafes_Success() {
    // given
    AuthUser authUser = createAuthUser(RoleType.ADMIN);
    User user = DummyUser.createDummyUser(authUser.getRoleType());
    List<Cafe> cafes = DummyCafe.createDummyCafes(user, 2);

    when(cafesSimpleCreateRequestDto.convertDtoToEntity(user)).thenReturn(cafes);
    when(userValidationCheck.validMy(authUser.getId())).thenReturn(user);

    // when
    StatusDto result = cafeService.creatCafe(authUser, cafesSimpleCreateRequestDto);

    // then
    assertEquals(HttpStatus.CREATED.value(), result.getStatus());
    assertEquals("카페 [2]개 등록 성공", result.getMessage());

    verify(userValidationCheck).validMy(authUser.getId());
    verify(cafesSimpleCreateRequestDto).convertDtoToEntity(user);
    verify(cafeRepository).saveAll(cafes);
  }

  @Test
  @DisplayName("카페 조회 성공")
  void searchCafe_Success() {
    // given
    CafeSearchCondition condition = mock(CafeSearchCondition.class);
    CafeResponseDto cafeResponseDto = new CafeResponseDto();
    CafeResponseDto cafeResponseDto2 = new CafeResponseDto();
    Page<CafeResponseDto> cafes = new PageImpl<>(List.of(cafeResponseDto, cafeResponseDto2));
    when(cafeRepository.findAllByCafe(condition)).thenReturn(cafes);

    // when
    PageResponseDto<CafeResponseDto> result = cafeService.searchCafe(condition);

    // than
    assertEquals(HttpStatus.OK.value(), result.getStatus());
    assertEquals("카페 조회 성공", result.getMessage());
    assertEquals(2, result.getData().size());
    verify(cafeRepository).findAllByCafe(condition);
  }

  @Test
  @DisplayName("카페 상세 조회 성공")
  void findCafe_Success() {
    // given
    Long cafeId = 1L;
    CafeResponseDto cafeResponseDto = CafeResponseDto.builder().name("Test Cafe").build();
    CafeDetailResponseDto cafeDetailResponseDto = new CafeDetailResponseDto();
    cafeDetailResponseDto.setCafeInfo(cafeResponseDto);
    when(cafeRepository.findCafeById(cafeId)).thenReturn(cafeResponseDto);

    // when
    ResponseDto<CafeDetailResponseDto> result = cafeService.findCafe(cafeId);

    // then
    assertEquals(HttpStatus.OK.value(), result.getStatus());
    assertEquals("[Test Cafe] 상세 조회 성공", result.getMessage());
    assertEquals("Test Cafe", result.getData().getName());
    verify(cafeRepository).findCafeById(cafeId);
  }

  @Test
  @DisplayName("카페 상세 조회 - 검색 결과가 없음")
  void findCafe_NotFound() {
    // given
    Long cafeId = 1L;
    when(cafeRepository.findCafeById(cafeId)).thenReturn(null);

    // when
    ResponseDto<CafeDetailResponseDto> result = cafeService.findCafe(cafeId);

    // then
    assertEquals(HttpStatus.OK.value(), result.getStatus());
    assertEquals("조회 결과가 없습니다.", result.getMessage());
    assertNull(result.getData());
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
    when(cafeValidationCheck.validMyCafe(cafeId, authUser.getId())).thenReturn(cafe);

    // when
    StatusDto result = cafeService.updateCafe(authUser, cafeId, cafeSimpleRequestDto);

    // then
    assertEquals(HttpStatus.OK.value(), result.getStatus());
    assertEquals("[" + cafe.getName() + "] 수정 성공", result.getMessage());
    verify(cafeValidationCheck).validMyCafe(cafeId, authUser.getId());
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
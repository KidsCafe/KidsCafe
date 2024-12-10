package com.sparta.kidscafe.domain.cafe.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.PageResponseDto;
import com.sparta.kidscafe.common.dto.ResponseDto;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.domain.cafe.dto.SearchCondition;
import com.sparta.kidscafe.domain.cafe.dto.request.CafeCreateRequestDto;
import com.sparta.kidscafe.domain.cafe.dto.response.CafeDetailResponseDto;
import com.sparta.kidscafe.domain.cafe.dto.response.CafeResponseDto;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.entity.CafeImage;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.domain.fee.entity.Fee;
import com.sparta.kidscafe.domain.fee.repository.FeeRepository;
import com.sparta.kidscafe.domain.pricepolicy.entity.PricePolicy;
import com.sparta.kidscafe.domain.pricepolicy.repository.PricePolicyRepository;
import com.sparta.kidscafe.domain.room.entity.Room;
import com.sparta.kidscafe.domain.room.repository.RoomRepository;
import com.sparta.kidscafe.domain.user.entity.User;
import com.sparta.kidscafe.domain.user.repository.UserRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class) // @Mock 사용을 위해 설정합니다.
public class CafeServiceTest {

  @Mock
  private CafeRepository cafeRepository;

  @Mock
  private RoomRepository roomRepository;

  @Mock
  private FeeRepository feeRepository;

  @Mock
  private PricePolicyRepository pricePolicyRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private CafeImageService cafeImageService;

  private AuthUser authUser;
  private User user;
  private CafeCreateRequestDto requestDto;
  private List<MultipartFile> cafeImages;

  @BeforeEach
  void setUp() {
    // 테스트를 위한 기본 User 및 Request DTO 생성
    authUser = new AuthUser(15L, "test@email.com", RoleType.OWNER);
    user = User.builder().id(authUser.getId()).build();
    requestDto = mock(CafeCreateRequestDto.class);
    cafeImages = Arrays.asList(mock(MultipartFile.class), mock(MultipartFile.class));
  }

  @Test
  @DisplayName("카페 등록 성공 - 사장님")
  void createCafeByOwner_success() {
    // given: CafeCreateRequestDto의 메서드들 mock 처리
    Cafe cafe = mock(Cafe.class);
    when(requestDto.convertDtoToEntityByCafe(user))
        .thenReturn(cafe);
    when(requestDto.convertDtoToEntityByRoom(cafe))
        .thenReturn(Collections.singletonList(mock(Room.class)));
    when(requestDto.convertDtoToEntityByFee(cafe))
        .thenReturn(Collections.singletonList(mock(Fee.class)));
    when(requestDto.convertDtoToEntityByPricePolicy(cafe))
        .thenReturn(Collections.singletonList(mock(PricePolicy.class)));
    when(cafe.getName())
        .thenReturn("Test Cafe");

    // when: createCafe 메서드 호출
    CafeService service = new CafeService(
        cafeRepository,
        roomRepository,
        feeRepository,
        pricePolicyRepository,
        userRepository,
        cafeImageService);

    // Mock 동작 설정: Repository 호출 시 Mock 결과 반환
    when(userRepository.findById(authUser.getId())).thenReturn(Optional.of(user));

    // when: 실행
    StatusDto result = service.createCafe(authUser, requestDto, cafeImages);

    // then: 결과 확인
    assert (result.getStatus() == HttpStatus.CREATED.value());
    assert (result.getMessage().equals("[Test Cafe] 등록 성공"));

    // cafeRepository, roomRepository, feeRepository, pricePolicyRepository, fileUtil이 각각 호출되었는지 검증
    verify(cafeRepository, times(1)).save(cafe);
    verify(roomRepository, times(1)).saveAll(anyList());
    verify(feeRepository, times(1)).saveAll(anyList());
    verify(pricePolicyRepository, times(1)).saveAll(anyList());
    verify(cafeImageService, times(1)).saveCafeImages(cafe, cafeImages);
  }

  @Test
  @DisplayName("카페 목록 조회 성공 - 사용자가 검색한")
  void searchCafe_success() {
    // Arrange: 테스트 입력값 및 Mock 동작 정의
    SearchCondition searchCondition = SearchCondition.builder()
        .name("Test Cafe")
        .region("Seoul")
        .pageable(PageRequest.of(0, 10))
        .build();

    List<CafeResponseDto> cafeResponseDtoList = Arrays.asList(
        new CafeResponseDto(
            1L,
            "Test Cafe",
            "Seoul",
            50,
            4.5,
            20L,
            "월",
            true,
            true,
            true,
            true,
            "http://..",
            null,
            null),
        new CafeResponseDto(
            2L,
            "Another Cafe",
            "Seoul",
            30,
            4.0,
            15L,
            "토, 일",
            true,
            true,
            false,
            true,
            "http://..",
            null,
            null)
    );

    Page<CafeResponseDto> mockPage = new PageImpl<>(cafeResponseDtoList,
        searchCondition.getPageable(), 2);

    // Mock 동작 설정: Repository 호출 시 Mock 결과 반환
    when(cafeRepository.findAllByCafe(any(SearchCondition.class))).thenReturn(mockPage);

    // Act: 테스트 대상 메서드 호출
    CafeService service = new CafeService(
        cafeRepository,
        roomRepository,
        feeRepository,
        pricePolicyRepository,
        userRepository,
        cafeImageService);
    PageResponseDto<CafeResponseDto> response = service.searchCafe(searchCondition);

    // Assert: 반환값 검증
    assertThat(response.getData()).hasSize(2); // 반환된 데이터 크기 확인
    assertThat(response.getData().get(0).getName()).isEqualTo("Test Cafe"); // 첫 번째 Cafe 이름 확인
    assertThat(response.getData().get(1).getName()).isEqualTo("Another Cafe"); // 두 번째 Cafe 이름 확인
  }

  @Test
  @DisplayName("카페 상세 조회 성공")
  void findCafe_success() {
    // given
    Long cafeId = 1L;
    CafeResponseDto cafeResponseDto = new CafeResponseDto(
        1L,
        "Test Cafe",
        "Seoul",
        50,
        4.5,
        20L,
        "월",
        true,
        true,
        true,
        true,
        "http://..",
        null,
        null);
    List<CafeImage> images = Collections.emptyList();
    List<Room> rooms = Collections.emptyList();
    List<Fee> fees = Collections.emptyList();
    List<PricePolicy> pricePolicies = Collections.emptyList();

    when(cafeRepository.findCafeById(cafeId)).thenReturn(cafeResponseDto);
    when(cafeImageService.searchCafeImage(cafeId)).thenReturn(images);
    when(roomRepository.findAllByCafeId(cafeId)).thenReturn(rooms);
    when(feeRepository.findAllByCafeId(cafeId)).thenReturn(fees);
    when(pricePolicyRepository.findAllByCafeId(cafeId)).thenReturn(pricePolicies);

    // when
    CafeService service = new CafeService(
        cafeRepository,
        roomRepository,
        feeRepository,
        pricePolicyRepository,
        userRepository,
        cafeImageService);
    ResponseDto<CafeDetailResponseDto> response = service.findCafe(cafeId);

    // then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getData()).isNotNull();
    assertThat(response.getMessage()).isEqualTo("[Test Cafe] 상세 조회 성공");
  }

  @Test
  @DisplayName("카페 상세 조회 - 조회 결과가 없음")
  void findCafe_notFound() {
    // given
    Long cafeId = 1L;
    when(cafeRepository.findCafeById(cafeId)).thenReturn(null);

    // when
    CafeService service = new CafeService(
        cafeRepository,
        roomRepository,
        feeRepository,
        pricePolicyRepository,
        userRepository,
        cafeImageService);
    ResponseDto<CafeDetailResponseDto> response = service.findCafe(cafeId);

    // then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getData()).isNull();
    assertThat(response.getMessage()).isEqualTo("조회 결과가 없습니다.");
  }
}

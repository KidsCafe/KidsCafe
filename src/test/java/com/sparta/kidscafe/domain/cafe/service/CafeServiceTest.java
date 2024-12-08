package com.sparta.kidscafe.domain.cafe.service;

import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.common.util.FileUtil;
import com.sparta.kidscafe.domain.cafe.dto.request.CafeCreateRequestDto;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.repository.CafeImageRepository;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.domain.fee.entity.Fee;
import com.sparta.kidscafe.domain.fee.repository.FeeRepository;
import com.sparta.kidscafe.domain.pricepolicy.entity.PricePolicy;
import com.sparta.kidscafe.domain.pricepolicy.repository.PricePolicyRepository;
import com.sparta.kidscafe.domain.room.entity.Room;
import com.sparta.kidscafe.domain.room.repository.RoomRepository;
import com.sparta.kidscafe.domain.user.entity.User;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class) // @Mock 사용을 위해 설정합니다.
public class CafeServiceTest {
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
  private FileUtil fileUtil;

  private User user;
  private CafeCreateRequestDto requestDto;
  private List<MultipartFile> cafeImages;

  @BeforeEach
  void setUp() {
    // 테스트를 위한 기본 User 및 Request DTO 생성
    user = User.builder().id(15L).build();
    requestDto = mock(CafeCreateRequestDto.class);
    cafeImages = Arrays.asList(mock(MultipartFile.class), mock(MultipartFile.class));
  }

  @Test
  @DisplayName("카페 등록 - 사장님")
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
        cafeImageRepository,
        roomRepository,
        feeRepository,
        pricePolicyRepository,
        fileUtil);
    StatusDto result = service.createCafe(user, requestDto, cafeImages);

    // then: 결과 확인
    assert(result.getStatus() == HttpStatus.CREATED.value());
    assert(result.getMessage().equals("[Test Cafe] 등록 성공"));

    // cafeRepository, roomRepository, feeRepository, pricePolicyRepository, fileUtil이 각각 호출되었는지 검증
    verify(cafeRepository, times(1)).save(cafe);
    verify(roomRepository, times(1)).saveAll(anyList());
    verify(feeRepository, times(1)).saveAll(anyList());
    verify(pricePolicyRepository, times(1)).saveAll(anyList());
    verify(fileUtil, times(1)).uploadCafeImage(cafeImages, cafe.getId());
  }
}

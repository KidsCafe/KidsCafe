package com.sparta.kidscafe.domain.cafe.service;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.PageResponseDto;
import com.sparta.kidscafe.common.dto.ResponseDto;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.common.util.ValidationCheck;
import com.sparta.kidscafe.domain.cafe.dto.request.CafeCreateRequestDto;
import com.sparta.kidscafe.domain.cafe.dto.request.CafeSimpleRequestDto;
import com.sparta.kidscafe.domain.cafe.dto.request.CafesSimpleCreateRequestDto;
import com.sparta.kidscafe.domain.cafe.dto.response.CafeDetailResponseDto;
import com.sparta.kidscafe.domain.cafe.dto.response.CafeResponseDto;
import com.sparta.kidscafe.domain.cafe.repository.condition.CafeCafeSearchCondition;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.entity.CafeImage;
import com.sparta.kidscafe.domain.cafe.repository.CafeImageRepository;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.domain.fee.entity.Fee;
import com.sparta.kidscafe.domain.fee.repository.FeeRepository;
import com.sparta.kidscafe.domain.pricepolicy.entity.PricePolicy;
import com.sparta.kidscafe.domain.pricepolicy.repository.PricePolicyRepository;
import com.sparta.kidscafe.domain.room.entity.Room;
import com.sparta.kidscafe.domain.room.repository.RoomRepository;
import com.sparta.kidscafe.domain.user.entity.User;
import com.sparta.kidscafe.domain.user.repository.UserRepository;
import com.sparta.kidscafe.exception.BusinessException;
import com.sparta.kidscafe.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CafeService {

  private final CafeRepository cafeRepository;
  private final CafeImageRepository cafeImageRepository;
  private final RoomRepository roomRepository;
  private final FeeRepository feeRepository;
  private final PricePolicyRepository pricePolicyRepository;
  private final UserRepository userRepository;

  @Transactional
  public StatusDto createCafe(AuthUser authUser, CafeCreateRequestDto requestDto) {
    User user = findByUserId(authUser.getId());
    Cafe cafe = saveCafe(requestDto, user);
    saveCafeImage(cafe, requestDto.getImages());
    saveCafeDetailInfo(requestDto, cafe);
    return createStatusDto(
        HttpStatus.CREATED,
        "[" + cafe.getName() + "] 등록 성공"
    );
  }

  public StatusDto creatCafe(AuthUser authUser, CafesSimpleCreateRequestDto requestDto) {
    User user = findByUserId(authUser.getId());
    List<Cafe> cafes = requestDto.convertDtoToEntity(user);
    cafeRepository.saveAll(cafes);
    return createStatusDto(
        HttpStatus.CREATED,
        "카페 [" + cafes.size() + "]개 등록 성공"
    );
  }

  public PageResponseDto<CafeResponseDto> searchCafe(CafeCafeSearchCondition condition) {
    Page<CafeResponseDto> cafes = cafeRepository.findAllByCafe(condition);
    return PageResponseDto.success(
        cafes,
        HttpStatus.OK,
        cafes.isEmpty() ? "조회 결과가 없습니다." : "카페 조회 성공"
    );
  }

  public ResponseDto<CafeDetailResponseDto> findCafe(Long cafeId) {
    CafeResponseDto cafeResponseDto = cafeRepository.findCafeById(cafeId);
    return ResponseDto.success(
        createCafeDetailInfo(cafeResponseDto),
        HttpStatus.OK,
        cafeResponseDto == null ?
            "조회 결과가 없습니다."
            : "[" + cafeResponseDto.getName() + "] 상세 조회 성공"
    );
  }

  @Transactional
  public StatusDto updateCafe(AuthUser authUser, Long cafeId, CafeSimpleRequestDto requestDto) {
    Cafe cafe = findByCafe(cafeId, authUser.getId());
    ValidationCheck.validMyCafe(authUser, cafe);
    cafe.update(requestDto);
    return createStatusDto(
        HttpStatus.OK,
        "[" + cafe.getName() + "] 수정 성공"
    );
  }

  @Transactional
  public void deleteCafe(AuthUser authUser, Long cafeId) {
    Cafe cafe = findByCafe(cafeId, authUser.getId());
    cafeRepository.delete(cafe);
    List<CafeImage> cafeImages = cafeImageRepository.findAllByCafeId(cafeId);
    for (CafeImage cafeImage : cafeImages) {
      cafeImage.delete();
    }
  }

  @Transactional
  public void deleteCafe(AuthUser authUser, List<Long> cafeIds) {
    List<Cafe> cafes = cafeRepository.findAllByUserIdAndIdIn(authUser.getId(), cafeIds);
    cafeRepository.deleteAll(cafes);
  }

  private User findByUserId(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
  }

  private Cafe findByCafe(Long cafeId, Long userId) {
    return cafeRepository.findByIdAndUserId(cafeId, userId)
        .orElseThrow(() -> new BusinessException(ErrorCode.CAFE_NOT_FOUND));
  }

  public void saveCafeImage(Cafe cafe, List<Long> images) {
    List<CafeImage> cafeImages = cafeImageRepository.findAllById(images);
    for (CafeImage cafeImage : cafeImages) {
      cafeImage.update(cafe.getId());
    }
  }

  private Cafe saveCafe(CafeCreateRequestDto requestDto, User user) {
    Cafe cafe = requestDto.convertDtoToEntityByCafe(user);
    cafeRepository.save(cafe);
    return cafe;
  }

  private void saveCafeDetailInfo(CafeCreateRequestDto requestDto, Cafe cafe) {
    List<Room> rooms = requestDto.convertDtoToEntityByRoom(cafe);
    List<Fee> fees = requestDto.convertDtoToEntityByFee(cafe);
    List<PricePolicy> pricePolicies = requestDto.convertDtoToEntityByPricePolicy(cafe);

    roomRepository.saveAll(rooms);
    feeRepository.saveAll(fees);
    pricePolicyRepository.saveAll(pricePolicies);
  }

  private CafeDetailResponseDto createCafeDetailInfo(CafeResponseDto cafeResponseDto) {
    if (cafeResponseDto == null) {
      return null;
    }

    Long cafeId = cafeResponseDto.getId();
    List<CafeImage> images = cafeImageRepository.findAllByCafeId(cafeId);
    List<Room> rooms = roomRepository.findAllByCafeId(cafeId);
    List<Fee> fees = feeRepository.findAllByCafeId(cafeId);
    List<PricePolicy> pricePolicies = pricePolicyRepository.findAllByCafeId(cafeId);

    CafeDetailResponseDto cafeDetailResponseDto = new CafeDetailResponseDto();
    cafeDetailResponseDto.setCafeInfo(cafeResponseDto);
    cafeDetailResponseDto.setCafeImage(images);
    cafeDetailResponseDto.setRooms(rooms);
    cafeDetailResponseDto.setFees(fees);
    cafeDetailResponseDto.setPricePolicies(pricePolicies);
    return cafeDetailResponseDto;
  }

  private StatusDto createStatusDto(HttpStatus status, String message) {
    return StatusDto.builder()
        .status(status.value())
        .message(message)
        .build();
  }
}

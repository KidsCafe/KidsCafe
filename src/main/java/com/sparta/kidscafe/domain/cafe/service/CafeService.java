package com.sparta.kidscafe.domain.cafe.service;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.PageResponseDto;
import com.sparta.kidscafe.common.dto.ResponseDto;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.common.util.FileUtil;
import com.sparta.kidscafe.domain.cafe.dto.SearchCondition;
import com.sparta.kidscafe.domain.cafe.dto.request.CafeCreateRequestDto;
import com.sparta.kidscafe.domain.cafe.dto.response.CafeDetailResponseDto;
import com.sparta.kidscafe.domain.cafe.dto.response.CafeResponseDto;
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
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CafeService {

  private final CafeRepository cafeRepository;
  private final CafeImageRepository cafeImageRepository;
  private final RoomRepository roomRepository;
  private final FeeRepository feeRepository;
  private final PricePolicyRepository pricePolicyRepository;
  private final UserRepository userRepository;
  private final FileUtil fileUtil;

  @Transactional
  public StatusDto createCafe(AuthUser authUser, CafeCreateRequestDto requestDto,
      List<MultipartFile> cafeImages) {
    User user = findByUserId(authUser.getId());
    Cafe cafe = saveCafe(requestDto, user);
    saveCafeImage(cafe, cafeImages);
    saveCafeDetailInfo(requestDto, cafe);
    return createStatusDto(
        HttpStatus.CREATED,
        "[" + cafe.getName() + "] 등록 성공"
    );
  }

  private User findByUserId(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
  }

  public PageResponseDto<CafeResponseDto> searchCafe(SearchCondition condition) {
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

  private CafeDetailResponseDto createCafeDetailInfo(CafeResponseDto cafeResponseDto) {
    if (cafeResponseDto == null)
      return null;

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

  private Cafe saveCafe(CafeCreateRequestDto requestDto, User user) {
    Cafe cafe = requestDto.convertDtoToEntityByCafe(user);
    cafeRepository.save(cafe);
    return cafe;
  }

  private void saveCafeImage(Cafe cafe, List<MultipartFile> cafeImages) {
    List<String> imagePaths = fileUtil.uploadCafeImage(cafeImages, cafe.getId());
    List<CafeImage> saveImages = new ArrayList<>();
    for (String imagePath : imagePaths) {
      CafeImage cafeImage = CafeImage.builder()
          .cafe(cafe)
          .imagePath(imagePath)
          .build();
      saveImages.add(cafeImage);
    }
    cafeImageRepository.saveAll(saveImages);
  }

  private void saveCafeDetailInfo(CafeCreateRequestDto requestDto, Cafe cafe) {
    List<Room> rooms = requestDto.convertDtoToEntityByRoom(cafe);
    List<Fee> fees = requestDto.convertDtoToEntityByFee(cafe);
    List<PricePolicy> pricePolicies = requestDto.convertDtoToEntityByPricePolicy(cafe);

    roomRepository.saveAll(rooms);
    feeRepository.saveAll(fees);
    pricePolicyRepository.saveAll(pricePolicies);
  }

  private StatusDto createStatusDto(HttpStatus status, String message) {
    return StatusDto.builder()
        .status(status.value())
        .message(message)
        .build();
  }
}

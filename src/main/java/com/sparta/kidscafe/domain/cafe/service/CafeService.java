package com.sparta.kidscafe.domain.cafe.service;

import com.sparta.kidscafe.api.address.MapService;
import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.PageResponseDto;
import com.sparta.kidscafe.common.dto.ResponseDto;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.common.util.valid.CafeValidationCheck;
import com.sparta.kidscafe.common.util.valid.UserValidationCheck;
import com.sparta.kidscafe.domain.cafe.dto.request.CafeRequestDto;
import com.sparta.kidscafe.domain.cafe.dto.request.CafeSimpleRequestDto;
import com.sparta.kidscafe.domain.cafe.dto.request.CafesSimpleRequestDto;
import com.sparta.kidscafe.domain.cafe.dto.response.CafeDetailResponseDto;
import com.sparta.kidscafe.domain.cafe.dto.response.CafeResponseDto;
import com.sparta.kidscafe.domain.cafe.dto.response.CafeSimpleResponseDto;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.entity.CafeImage;
import com.sparta.kidscafe.domain.cafe.repository.CafeImageRepository;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.domain.cafe.repository.condition.CafeSearchCondition;
import com.sparta.kidscafe.domain.fee.entity.Fee;
import com.sparta.kidscafe.domain.fee.repository.FeeRepository;
import com.sparta.kidscafe.domain.lesson.entity.Lesson;
import com.sparta.kidscafe.domain.lesson.repository.LessonRepository;
import com.sparta.kidscafe.domain.pricepolicy.entity.PricePolicy;
import com.sparta.kidscafe.domain.pricepolicy.repository.PricePolicyRepository;
import com.sparta.kidscafe.domain.room.entity.Room;
import com.sparta.kidscafe.domain.room.repository.RoomRepository;
import com.sparta.kidscafe.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
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
  private final LessonRepository lessonRepository;
  private final FeeRepository feeRepository;
  private final PricePolicyRepository pricePolicyRepository;

  private final UserValidationCheck userValidationCheck;
  private final CafeValidationCheck cafeValidationCheck;

  private final MapService mapService;

  @Transactional
  public StatusDto createCafe(AuthUser authUser, CafeRequestDto requestDto) {
    User user = userValidationCheck.validMy(authUser.getId());
    Cafe cafe = saveCafe(requestDto, user);
    saveCafeImage(cafe, requestDto.getImages());
    saveCafeDetailInfo(requestDto, cafe);
    return StatusDto.createStatusDto(
        HttpStatus.CREATED,
        "[" + cafe.getName() + "] 등록 성공"
    );
  }

  public StatusDto creatCafe(AuthUser authUser, CafesSimpleRequestDto requestDto) {
    User user = userValidationCheck.validMy(authUser.getId());
    List<Cafe> cafes = requestDto.convertDtoToEntity(user);
    for(Cafe cafe : cafes) {
      Point location = mapService.convertAddressToGeo(cafe.getAddress());
      cafe.updateLocation(location);
    }

    cafeRepository.saveAll(cafes);
    return StatusDto.createStatusDto(
        HttpStatus.CREATED,
        "카페 [" + cafes.size() + "]개 등록 성공"
    );
  }

  public PageResponseDto<CafeSimpleResponseDto> searchCafe(CafeSearchCondition condition) {
    Page<CafeSimpleResponseDto> cafes = cafeRepository.findAllByCafeSimple(condition);
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
  public StatusDto updateCafe(AuthUser authUser, Long cafeId, CafeSimpleRequestDto requestDto)  {
    Point location = mapService.convertAddressToGeo(requestDto.getAddress());
    Cafe cafe = cafeValidationCheck.validMyCafe(cafeId, authUser.getId());
    cafe.update(requestDto, location);
    return StatusDto.createStatusDto(
        HttpStatus.OK,
        "[" + cafe.getName() + "] 수정 성공"
    );
  }

  @Transactional
  public void deleteCafe(AuthUser authUser, Long cafeId) {
    Cafe cafe = cafeValidationCheck.validMyCafe(cafeId, authUser.getId());
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

  public void saveCafeImage(Cafe cafe, List<Long> images) {
    List<CafeImage> cafeImages = cafeImageRepository.findAllById(images);
    for (CafeImage cafeImage : cafeImages) {
      cafeImage.update(cafe.getId());
    }
  }

  private Cafe saveCafe(CafeRequestDto requestDto, User user) {
    Point location = mapService.convertAddressToGeo(requestDto.getAddress());
    Cafe cafe = requestDto.convertDtoToEntityByCafe(user, location);
    cafeRepository.save(cafe);
    return cafe;
  }

  private void saveCafeDetailInfo(CafeRequestDto requestDto, Cafe cafe) {
    List<Room> rooms = requestDto.convertDtoToEntityByRoom(cafe);
    List<Lesson> lessons = requestDto.convertDtoToEntityByLesson(cafe);
    List<Fee> fees = requestDto.convertDtoToEntityByFee(cafe);
    List<PricePolicy> pricePolicies = requestDto.convertDtoToEntityByPricePolicy(cafe);

    roomRepository.saveAll(rooms);
    lessonRepository.saveAll(lessons);
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
    List<Lesson> lessons = lessonRepository.findAllByCafeId(cafeId);
    List<Fee> fees = feeRepository.findAllByCafeId(cafeId);
    List<PricePolicy> pricePolicies = pricePolicyRepository.findAllByCafeId(cafeId);

    CafeDetailResponseDto cafeDetailResponseDto = new CafeDetailResponseDto();
    cafeDetailResponseDto.setCafeInfo(cafeResponseDto);
    cafeDetailResponseDto.setCafeImage(images);
    cafeDetailResponseDto.setRooms(rooms);
    cafeDetailResponseDto.setLessons(lessons);
    cafeDetailResponseDto.setFees(fees);
    cafeDetailResponseDto.setPricePolicies(pricePolicies);
    return cafeDetailResponseDto;
  }
}

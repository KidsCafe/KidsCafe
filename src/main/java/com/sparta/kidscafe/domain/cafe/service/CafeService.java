package com.sparta.kidscafe.domain.cafe.service;

import com.sparta.kidscafe.api.kakao.MapService;
import com.sparta.kidscafe.common.dto.AuthUser;
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
import com.sparta.kidscafe.domain.user.entity.User;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CafeService {

  private final CafeRepository cafeRepository;
  private final CafeImageRepository cafeImageRepository;
  private final UserValidationCheck userValidationCheck;
  private final CafeValidationCheck cafeValidationCheck;
  private final MapService mapService;

  @Transactional
  public void createCafe(AuthUser authUser, CafeRequestDto requestDto) {
    User user = userValidationCheck.findUser(authUser.getId());
    cafeValidationCheck.validOverMaximum(authUser.getId());
    Cafe cafe = saveCafe(requestDto, user);
    saveCafeImage(cafe, requestDto.getImages());
    saveCafeDetailInfo(requestDto, cafe);
  }

  public void creatCafe(AuthUser authUser, List<CafeSimpleRequestDto> requestCafes) {
    if(requestCafes == null || requestCafes.isEmpty()) {
      return;
    }

    User user = userValidationCheck.findUser(authUser.getId());
    List<Cafe> cafes = new ArrayList<>();
    for(CafeSimpleRequestDto requestCafe : requestCafes) {
      Point location = mapService.convertAddressToGeo(requestCafe.getAddress());
      Cafe cafe = requestCafe.convertDtoToEntity(user, location);
      cafes.add(cafe);
    }
    cafeRepository.saveAll(cafes);
  }

  public Page<CafeSimpleResponseDto> searchCafe(CafeSearchCondition condition) {
    return cafeRepository.findAllByCafeSimple(condition);
  }

  public CafeDetailResponseDto findCafe(Long cafeId) {
    CafeResponseDto cafeResponseDto = cafeRepository.findCafeById(cafeId);
    return findCafeDetailInfo(cafeResponseDto);
  }

  @Transactional
  public void updateCafe(AuthUser authUser, Long cafeId, CafeSimpleRequestDto requestDto)  {
    Point location = mapService.convertAddressToGeo(requestDto.getAddress());
    Cafe cafe = cafeValidationCheck.validMyCafe(cafeId, authUser.getId());
    cafe.update(requestDto, location);
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

  private Cafe saveCafe(CafeRequestDto requestDto, User user) {
    Point location = mapService.convertAddressToGeo(requestDto.getAddress());
    Cafe cafe = requestDto.convertDtoToEntity(user, location);
    cafeRepository.save(cafe);
    return cafe;
  }

  public void saveCafeImage(Cafe cafe, List<Long> images) {
    List<CafeImage> cafeImages = cafeImageRepository.findAllById(images);
    for (CafeImage cafeImage : cafeImages) {
      cafeImage.update(cafe.getId());
    }
  }

  private void saveCafeDetailInfo(CafeRequestDto requestDto, Cafe cafe) {
    cafe.initRooms(requestDto.convertDtoToEntityByRoom(cafe));
    cafe.initLessons(requestDto.convertDtoToEntityByLesson(cafe));
    cafe.initFees(requestDto.convertDtoToEntityByFee(cafe));
    cafe.initPricePolicies(requestDto.convertDtoToEntityByPricePolicy(cafe));
  }

  private CafeDetailResponseDto findCafeDetailInfo(CafeResponseDto cafeResponseDto) {
    if (cafeResponseDto == null) {
      return null;
    }

    Long cafeId = cafeResponseDto.getId();
    Cafe cafe = cafeValidationCheck.findCafe(cafeId);
    CafeDetailResponseDto cafeDetailResponseDto = new CafeDetailResponseDto();
    cafeDetailResponseDto.setCafeInfo(cafeResponseDto);
    cafeDetailResponseDto.setCafeImage(cafeImageRepository.findAllByCafeId(cafeId));
    cafeDetailResponseDto.setRooms(cafe.getRooms());
    cafeDetailResponseDto.setLessons(cafe.getLessons());
    cafeDetailResponseDto.setFees(cafe.getFees());
    cafeDetailResponseDto.setPricePolicies(cafe.getPricePolicies());
    return cafeDetailResponseDto;
  }
}
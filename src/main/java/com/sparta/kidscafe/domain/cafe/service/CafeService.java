package com.sparta.kidscafe.domain.cafe.service;

import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.common.util.FileUtil;
import com.sparta.kidscafe.domain.cafe.dto.request.CafeCreateRequestDto;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.domain.fee.entity.Fee;
import com.sparta.kidscafe.domain.fee.repository.FeeRepository;
import com.sparta.kidscafe.domain.pricepolicy.entity.PricePolicy;
import com.sparta.kidscafe.domain.pricepolicy.repository.PricePolicyRepository;
import com.sparta.kidscafe.domain.room.entity.Room;
import com.sparta.kidscafe.domain.room.repository.RoomRepository;
import com.sparta.kidscafe.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CafeService {
  private final CafeRepository cafeRepository;
  private final RoomRepository roomRepository;
  private final FeeRepository feeRepository;
  private final PricePolicyRepository pricePolicyRepository;
  private final FileUtil fileUtil;

  @Transactional
  public StatusDto createCafe(User user, CafeCreateRequestDto requestDto, List<MultipartFile> cafeImages) {
    Cafe cafe = requestDto.convertDtoToEntityByCafe(user);
    cafeRepository.save(cafe);
    fileUtil.uploadCafeImage(cafeImages, cafe.getId());

    List<Room> rooms = requestDto.convertDtoToEntityByRoom(cafe);
    roomRepository.saveAll(rooms);

    List<Fee> fees = requestDto.convertDtoToEntityByFee(cafe);
    feeRepository.saveAll(fees);

    List<PricePolicy> pricePolicies = requestDto.convertDtoToEntityByPricePolicy(cafe);
    pricePolicyRepository.saveAll(pricePolicies);

    return StatusDto.builder()
        .status(HttpStatus.CREATED.value())
        .message("["+ cafe.getName() + "] 등록 성공")
        .build();
  }
}

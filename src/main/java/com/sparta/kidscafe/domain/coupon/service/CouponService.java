package com.sparta.kidscafe.domain.coupon.service;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.ListResponseDto;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.domain.coupon.dto.request.CouponCreateRequestDto;
import com.sparta.kidscafe.domain.coupon.dto.response.CouponResponseDto;
import com.sparta.kidscafe.domain.coupon.entity.Coupon;
import com.sparta.kidscafe.domain.coupon.repository.CouponRepository;
import com.sparta.kidscafe.domain.user.entity.User;
import com.sparta.kidscafe.domain.user.repository.UserRepository;
import com.sparta.kidscafe.exception.BusinessException;
import com.sparta.kidscafe.exception.ErrorCode;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService {

  private final CouponRepository couponRepository;
  private final CafeRepository cafeRepository;
  private final UserRepository userRepository;

  private void validationOwner(AuthUser authUser, Cafe cafe) {
    RoleType roleType = authUser.getRoleType();

    if(roleType == RoleType.ADMIN) {
      return;
    }

    if(roleType == RoleType.USER) {
      throw new BusinessException(ErrorCode.COUPON_TABLE_UNAUTHORIZED);
    }

    if(roleType == RoleType.OWNER && !authUser.getId().equals(cafe.getUser().getId())) {
      throw new BusinessException(ErrorCode.COUPON_TABLE_OWN_CREATE);
    }
  }

  @Transactional
  public StatusDto createCoupon(AuthUser authUser, Long cafeId, CouponCreateRequestDto couponCreateRequestDto) {
    Cafe cafe = cafeRepository.findById(cafeId)
            .orElseThrow(() -> new BusinessException(ErrorCode.CAFE_NOT_FOUND));

    validationOwner(authUser, cafe);
    Coupon coupon = couponCreateRequestDto.convertToEntity(cafe);

    couponRepository.save(coupon);

    return StatusDto.builder()
            .status(HttpStatus.CREATED.value())
            .message(coupon.getName() + "생성")
            .build();
  }

  public ListResponseDto<CouponResponseDto> getCouponByUser(AuthUser authUser){
    List<Coupon> coupons = couponRepository.findByCafeId(authUser.getId());
    return ListResponseDto.success(
        coupons.stream()
            .map(CouponResponseDto::from)
            .collect(Collectors.toList()), HttpStatus.OK, "조회");
  }

  public ListResponseDto<CouponResponseDto> getCouponByOwner(AuthUser authUser, Long cafeId){
    Cafe cafe = cafeRepository.findById(cafeId)
        .orElseThrow(() -> new BusinessException(ErrorCode.CAFE_NOT_FOUND));

    validationOwner(authUser, cafe);

    List<Coupon> coupons = couponRepository.findByCafeId(cafeId);
    return ListResponseDto.success(
        coupons.stream()
            .map(CouponResponseDto::from)
            .collect(Collectors.toList()), HttpStatus.OK, "조회");
  }

  @Transactional
  public StatusDto assignCouponToUser(Long couponId, Long userId){
    Coupon coupon = couponRepository.findById(couponId)
        .orElseThrow(() -> new BusinessException(ErrorCode.COUPON_NOT_FOUND));

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

    coupon.assignToUser(user);

    return StatusDto.builder()
        .status(HttpStatus.OK.value())
        .message("쿠폰이 지급되었습니다.")
        .build();
  }

  @Transactional
  public StatusDto useCoupon(AuthUser authUser, Long couponId) {
    Coupon coupon = couponRepository.findById(couponId)
        .orElseThrow(() -> new BusinessException(ErrorCode.COUPON_NOT_FOUND));

    if(!authUser.getId().equals(coupon.getUser().getId())){
      throw new BusinessException(ErrorCode.COUPON_NOT_OWNED);
    }

    if(coupon.isUsed()){
      throw new BusinessException(ErrorCode.COUPON_ALREADY_USED);
    }

    coupon.markAsUsed();

    return StatusDto.builder()
        .status(HttpStatus.OK.value())
        .message("쿠폰 사용 완료")
        .build();
  }
}

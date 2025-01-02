package com.sparta.kidscafe.domain.coupon.service;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.domain.coupon.dto.request.FirstComeCouponCreateRequestDto;
import com.sparta.kidscafe.domain.coupon.entity.FirstComeCoupon;
import com.sparta.kidscafe.domain.coupon.repository.FirstComeCouponRepository;
import com.sparta.kidscafe.exception.BusinessException;
import com.sparta.kidscafe.exception.ErrorCode;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FirstComeCouponService {

  private final FirstComeCouponRepository firstComeCouponRepository;
  private final CafeRepository cafeRepository;

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

  public StatusDto createCoupon(AuthUser authUser, Long cafeId, FirstComeCouponCreateRequestDto firstComeCouponCreateRequestDto) {
    Cafe cafe = cafeRepository.findById(cafeId)
        .orElseThrow(() -> new BusinessException(ErrorCode.CAFE_NOT_FOUND));

    validationOwner(authUser, cafe);
    FirstComeCoupon firstComeCoupon = firstComeCouponCreateRequestDto.convertToEntity(cafe);

    firstComeCouponRepository.save(firstComeCoupon);
    return StatusDto.builder()
        .status(HttpStatus.CREATED.value())
        .message(firstComeCoupon.getName() + "생성")
        .build();
  }

  // 낙관적 락 적용 선착순 쿠폰 뿌리기
  @Transactional
  public StatusDto issueCoupon(AuthUser authUser, Long couponId) {
    for (int i = 0; i < 3; i++) { // 최대 3번 재시도
      try {
        FirstComeCoupon firstComeCoupon = firstComeCouponRepository.findById(couponId)
            .orElseThrow(() -> new BusinessException(ErrorCode.COUPON_NOT_FOUND));

        firstComeCoupon.issueCoupon(); // 쿠폰 발급
        firstComeCouponRepository.save(firstComeCoupon); // 발급 수량 업데이트
        return StatusDto.builder()
            .status(HttpStatus.OK.value())
            .message("쿠폰 발급 성공! 현재 발급 수량: "
                + firstComeCoupon.getIssuedQuantity()
                + "/"
                + firstComeCoupon.getMaxQuantity())
            .build();
      } catch (OptimisticLockException e) {
        try {
          Thread.sleep(50); // 잠시 대기 후 재시도
        } catch (InterruptedException ex) {
          Thread.currentThread().interrupt();
        }
      }
    }
    throw new IllegalStateException("쿠폰 발급 중 충돌이 발생하여 실패했습니다.");
  }
}

package com.sparta.kidscafe.domain.coupon.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.domain.coupon.dto.request.CouponCreateRequestDto;
import com.sparta.kidscafe.domain.coupon.entity.Coupon;
import com.sparta.kidscafe.domain.coupon.repository.CouponRepository;
import com.sparta.kidscafe.domain.user.entity.User;
import com.sparta.kidscafe.domain.user.repository.UserRepository;
import com.sparta.kidscafe.exception.BusinessException;
import com.sparta.kidscafe.exception.ErrorCode;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

  @Mock
  private CouponRepository couponRepository;

  @Mock
  private CafeRepository cafeRepository;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private CouponService couponService;

  @Test
  @DisplayName("쿠폰 생성: OWNER 권한-본인가게-성공")
  void createCoupon_owner_success(){
    //given
    AuthUser authUser = new AuthUser(1L, "owner@test.com", RoleType.OWNER);
    Cafe cafe = Cafe.builder()
        .id(1L)
        .user(authUser.toUser())
        .build();

    CouponCreateRequestDto couponCreateRequestDto = new CouponCreateRequestDto("Coupon", 10,
        LocalDateTime.parse("2025-01-01T23:59:59"));

    when(cafeRepository.findById(1L)).thenReturn(Optional.of(cafe));

    // when
    couponService.createCoupon(authUser, 1L, couponCreateRequestDto);

    // then
    verify(cafeRepository, times(1)).findById(1L);
    verify(couponRepository, times(1)).save(any(Coupon.class));
  }

  @Test
  @DisplayName("쿠폰 생성: OWNER 권한-본인 가게 X-실패")
  void createCoupon_owner_fail(){
    // given
    AuthUser authUser = new AuthUser(1L, "owner@test.com", RoleType.OWNER);
    Cafe otherCafe = Cafe.builder()
        .id(2L)
        .user(mock(User.class))
        .build();

    CouponCreateRequestDto couponCreateRequestDto = new CouponCreateRequestDto("Coupon", 20,
        LocalDateTime.parse("2025-01-01T23:59:59"));

    when(cafeRepository.findById(2L)).thenReturn(Optional.of(otherCafe));

    // when // then
    assertThatThrownBy(() -> couponService.createCoupon(authUser, 2L, couponCreateRequestDto))
        .isInstanceOf(BusinessException.class)
        .hasMessageContaining(ErrorCode.COUPON_TABLE_OWN_CREATE.getMessage());

    verify(cafeRepository, times(1)).findById(2L);
    verify(couponRepository, never()).save(any());
  }

  @Test
  @DisplayName("쿠폰 생성: USER 권한-실패")
  void createCoupon_user_fail(){
    // given
    AuthUser authUser = new AuthUser(1L, "user@test.com", RoleType.USER);
    Cafe mockCafe = mock(Cafe.class);

    when(cafeRepository.findById(1L)).thenReturn(Optional.of(mockCafe));

    CouponCreateRequestDto couponCreateRequestDto = new CouponCreateRequestDto("Coupon", 20,
        LocalDateTime.parse("2025-01-01T23:59:59"));

    // when // then
    assertThatThrownBy(() -> couponService.createCoupon(authUser, 1L, couponCreateRequestDto))
        .isInstanceOf(BusinessException.class)
        .hasMessageContaining(ErrorCode.COUPON_TABLE_UNAUTHORIZED.getMessage());
  }

  @Test
  @DisplayName("쿠폰 사용: 본인 쿠폰-성공")
  void useCoupon_owner_success(){
    // given
    AuthUser authUser = new AuthUser(1L, "user@test.com", RoleType.USER);
    User user = authUser.toUser();
    Coupon coupon = Coupon.builder()
        .id(1L)
        .user(user)
        .isUsed(false)
        .build();

    when(couponRepository.findById(1L)).thenReturn(Optional.of(coupon));

    // when
    couponService.useCoupon(authUser, 1L);

    // then
    verify(couponRepository, times(1)).findById(1L);
  }

  @Test
  @DisplayName("쿠폰 사용: 본인 쿠폰 X-실패")
  void useCoupon_fail_not_owned(){
    // given
    AuthUser authUser = new AuthUser(1L, "user@test.com", RoleType.USER);
    Coupon otherCoupon = Coupon.builder()
        .id(1L)
        .user(mock(User.class))
        .isUsed(false)
        .build();

    when(couponRepository.findById(1L)).thenReturn(Optional.of(otherCoupon));

    // when // then
    assertThatThrownBy(() -> couponService.useCoupon(authUser, 1L))
        .isInstanceOf(BusinessException.class)
        .hasMessageContaining(ErrorCode.COUPON_NOT_OWNED.getMessage());

    verify(couponRepository, times(1)).findById(1L);
  }

  @Test
  @DisplayName("쿠폰 사용: 이미 사용된 쿠폰-실패")
  void useCoupon_fail_already_used(){
    // given
    AuthUser authUser = new AuthUser(1L, "user@test.com", RoleType.USER);
    User user = authUser.toUser();
    Coupon usedCoupon = Coupon.builder()
        .id(1L)
        .user(user)
        .isUsed(true)
        .build();

    when(couponRepository.findById(1L)).thenReturn(Optional.of(usedCoupon));

    // when // then
    assertThatThrownBy(() -> couponService.useCoupon(authUser, 1L))
        .isInstanceOf(BusinessException.class)
        .hasMessageContaining(ErrorCode.COUPON_ALREADY_USED.getMessage());

    verify(couponRepository, times(1)).findById(1L);
  }
}
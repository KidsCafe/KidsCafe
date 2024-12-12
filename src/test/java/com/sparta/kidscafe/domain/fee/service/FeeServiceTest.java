package com.sparta.kidscafe.domain.fee.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.enums.AgeGroup;
import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.domain.fee.dto.request.FeeCreateRequestDto;
import com.sparta.kidscafe.domain.fee.entity.Fee;
import com.sparta.kidscafe.domain.fee.repository.FeeRepository;
import com.sparta.kidscafe.domain.user.entity.User;
import com.sparta.kidscafe.exception.BusinessException;
import com.sparta.kidscafe.exception.ErrorCode;

@ExtendWith(MockitoExtension.class)
public class FeeServiceTest {

	// private static final Logger log = LoggerFactory.getLogger(FeeServiceTest.class);

	@Mock
	private FeeRepository feeRepository;

	@Mock
	private CafeRepository cafeRepository;

	@InjectMocks
	private FeeService feeService;

	@Test
	@DisplayName("가격표 생성: OWNER 권한-본인가게-성공")
	void createFee_owner_success(){
		// given
		AuthUser authUser = new AuthUser(1L, "owner@test.com", RoleType.OWNER);
		Cafe cafe = Cafe.builder()
			.id(1L)
			.user(authUser.toUser())
			.build();

		FeeCreateRequestDto feeCreateRequestDto = new FeeCreateRequestDto(AgeGroup.CHILDREN, 10000);

		when(cafeRepository.findById(1L)).thenReturn(Optional.of(cafe));
		when(feeRepository.save(any(Fee.class))).thenReturn(mock(Fee.class));

		// when
		feeService.createFee(authUser, 1L, feeCreateRequestDto);

		// then
		verify(cafeRepository, times(1)).findById(1L);
		verify(feeRepository, times(1)).save(any(Fee.class));
	}

	@Test
	@DisplayName("가격표 생성: OWNER 권한-본인 가게 X-실패")
	void createFee_owner_fail(){
		// given
		AuthUser authUser = new AuthUser(1L, "owner@test.com", RoleType.OWNER);
		Cafe otherCafe = Cafe.builder()
			.id(2L)
			.user(mock(User.class))
			.build();

		FeeCreateRequestDto feeCreateRequestDto = new FeeCreateRequestDto(AgeGroup.TEENAGER, 10000);

		when(cafeRepository.findById(2L)).thenReturn(Optional.of(otherCafe));

		// when // then
		assertThatThrownBy(() -> feeService.createFee(authUser, 2L, feeCreateRequestDto))
			.isInstanceOf(BusinessException.class)
			.hasMessageContaining(String.valueOf(ErrorCode.FEE_TABLE_OWN_CREATE));

		verify(cafeRepository, times(1)).findById(2L);
		verify(feeRepository, never()).save(any());
	}

	@Test
	@DisplayName("가격표 생성: USER 권한 - 실패")
	void createFee_user_fail(){
		// given
		AuthUser authUser = new AuthUser(1L, "user@test.com", RoleType.USER);
		FeeCreateRequestDto feeCreateRequestDto = new FeeCreateRequestDto(AgeGroup.CHILDREN, 10000);

		// when // then
		assertThatThrownBy(() -> feeService.createFee(authUser, 1L, feeCreateRequestDto))
			.isInstanceOf(BusinessException.class)
			.hasMessageContaining(String.valueOf(ErrorCode.FEE_TABLE_UNAUTHORIZED));

		verify(cafeRepository, never()).findById(any());
		verify(feeRepository, never()).save(any());
	}


}

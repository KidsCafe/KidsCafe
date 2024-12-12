package com.sparta.kidscafe.domain.fee.service;

import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.enums.AgeGroup;
import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.domain.fee.dto.request.FeeCreateRequestDto;
import com.sparta.kidscafe.domain.fee.entity.Fee;
import com.sparta.kidscafe.domain.fee.repository.FeeRepository;


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
	@DisplayName("가격표 생성 : OWNER 권한 - 성공")
	void createFee_owner_success(){
		// given
		AuthUser authUser = new AuthUser(1L, "owner@test.com", RoleType.OWNER);
		Cafe cafe = Cafe.builder()
			.id(1L)
			.user(authUser.toUser())
			.build();

		FeeCreateRequestDto feeCreateRequestDto = new FeeCreateRequestDto(AgeGroup.CHILDREN, 1000);

		when(cafeRepository.findById(1L)).thenReturn(Optional.of(cafe));
		when(feeRepository.save(any(Fee.class))).thenReturn(mock(Fee.class));

		// when
		feeService.createFee(authUser, 1L, feeCreateRequestDto);

		// then
		verify(cafeRepository, times(1)).findById(1L);
		verify(feeRepository, times(1)).save(any(Fee.class));

	}
}

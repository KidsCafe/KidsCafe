package com.sparta.kidscafe.domain.fee.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.domain.fee.dto.request.FeeCreateRequestDto;
import com.sparta.kidscafe.domain.fee.dto.request.FeeUpdateRequestDto;
import com.sparta.kidscafe.domain.fee.dto.response.FeeResponseDto;
import com.sparta.kidscafe.domain.fee.entity.Fee;
import com.sparta.kidscafe.domain.fee.repository.FeeRepository;
import com.sparta.kidscafe.exception.BusinessException;
import com.sparta.kidscafe.exception.ErrorCode;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeeService {

	private final FeeRepository feeRepository;
	private final CafeRepository cafeRepository;

	// 권한 확인
	private void validationOwner(AuthUser authUser, Cafe cafe) {
		RoleType roleType = authUser.getRoleType();
		if(roleType == RoleType.ADMIN){
			return;
		}

		if(roleType == RoleType.OWNER && !authUser.getId().equals(cafe.getUser().getId())) {
			throw new BusinessException(ErrorCode.FEE_TABLE_OWN_CREATE);
		}

		if(roleType == RoleType.USER){
			throw new BusinessException(ErrorCode.FEE_TABLE_UNAUTHORIZED);
		}
	}

	@Transactional
	public FeeResponseDto createFee(AuthUser authUser, Long cafeId, FeeCreateRequestDto feeCreateRequestDto) {
		Cafe cafe = cafeRepository.findById(cafeId)
			.orElseThrow(() -> new BusinessException(ErrorCode.CAFE_NOT_FOUND));

		validationOwner(authUser, cafe);

		Fee fee = feeCreateRequestDto.convertDtoToEntity(cafe);
		Fee savedFee = feeRepository.save(fee);
		return FeeResponseDto.from(savedFee);
	}

	public List<FeeResponseDto> getFeesByCafe(Long cafeId) {
		List<Fee> fees = feeRepository.findByCafeId(cafeId);
		return fees.stream()
			.map(FeeResponseDto::from)
			.collect(Collectors.toList());
	}

	@Transactional
	public FeeResponseDto updateFee(AuthUser authUser, Long cafeId, Long feeId, FeeUpdateRequestDto feeUpdateRequestDto) {
		Cafe cafe = cafeRepository.findById(cafeId)
			.orElseThrow(() -> new BusinessException(ErrorCode.CAFE_NOT_FOUND));

		Fee fee = feeRepository.findById(feeId)
			.orElseThrow(() -> new BusinessException(ErrorCode.FEE_NOT_FOUND));

		validationOwner(authUser, cafe);

		fee.update(feeUpdateRequestDto);
		return FeeResponseDto.from(fee);
	}
}

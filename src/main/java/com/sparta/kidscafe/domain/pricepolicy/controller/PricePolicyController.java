package com.sparta.kidscafe.domain.pricepolicy.controller;

import com.sparta.kidscafe.common.dto.ListResponseDto;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.domain.pricepolicy.dto.request.PricePolicyCreateRequestDto;
import com.sparta.kidscafe.domain.pricepolicy.dto.request.PricePolicyUpdateRequestDto;
import com.sparta.kidscafe.domain.pricepolicy.dto.response.PricePolicyResponseDto;
import com.sparta.kidscafe.domain.pricepolicy.service.PricePolicyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cafes/{cafeId}/prices")
@RequiredArgsConstructor
public class PricePolicyController {

    private final PricePolicyService pricePolicyService;

    @PostMapping
    public ResponseEntity<StatusDto> addPricePolicy(
            @PathVariable Long cafeId,
            @RequestBody @Valid PricePolicyCreateRequestDto requestDto
    ) {
        StatusDto statusDto = pricePolicyService.addPricePolicy(cafeId, requestDto);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(statusDto);
    }

    @GetMapping
    public ResponseEntity<ListResponseDto<PricePolicyResponseDto>> getPricePolicies(@PathVariable Long cafeId) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(pricePolicyService.getPricePolicies(cafeId));
    }

    @PatchMapping("/{priceId}")
    public ResponseEntity<StatusDto> updatePricePolicy(
            @PathVariable Long cafeId,
            @PathVariable Long priceId,
            @RequestBody @Valid PricePolicyUpdateRequestDto requestDto
    ) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(pricePolicyService.updatePricePolicy(cafeId, priceId, requestDto));
    }

    @DeleteMapping("/{priceId}")
    public ResponseEntity<Void> deletePricePolicy(
            @PathVariable Long cafeId,
            @PathVariable Long priceId
    ) {
        pricePolicyService.deletePricePolicy(cafeId, priceId);
        return ResponseEntity.noContent().build();
    }
}

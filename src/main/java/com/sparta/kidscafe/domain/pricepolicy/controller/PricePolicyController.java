package com.sparta.kidscafe.domain.pricepolicy.controller;

import com.sparta.kidscafe.domain.pricepolicy.dto.request.PricePolicyCreateRequestDto;
import com.sparta.kidscafe.domain.pricepolicy.dto.request.PricePolicyUpdateRequestDto;
import com.sparta.kidscafe.domain.pricepolicy.dto.response.PricePolicyResponseDto;
import com.sparta.kidscafe.domain.pricepolicy.service.PricePolicyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cafes/{cafeId}/prices")
@RequiredArgsConstructor
public class PricePolicyController {

    private final PricePolicyService pricePolicyService;

    @PostMapping
    public ResponseEntity<String> addPricePolicy(
            @PathVariable Long cafeId,
            @RequestBody @Valid PricePolicyCreateRequestDto requestDto
    ) {
        pricePolicyService.addPricePolicy(cafeId, requestDto);
        return ResponseEntity.status(201).body("가격 정책이 추가되었습니다.");
    }

    @GetMapping
    public ResponseEntity<List<PricePolicyResponseDto>> getPricePolicies(@PathVariable Long cafeId) {
        List<PricePolicyResponseDto> policies = pricePolicyService.getPricePolicies(cafeId);
        return ResponseEntity.ok(policies);
    }

    @PatchMapping("/{pricesId}")
    public ResponseEntity<String> updatePricePolicy(
            @PathVariable Long cafeId,
            @PathVariable Long pricesId,
            @RequestBody @Valid PricePolicyUpdateRequestDto requestDto
    ) {
        pricePolicyService.updatePricePolicy(cafeId, pricesId, requestDto);
        return ResponseEntity.ok("가격 정책이 수정되었습니다.");
    }

    @DeleteMapping("/{pricesId}")
    public ResponseEntity<String> deletePricePolicy(
            @PathVariable Long cafeId,
            @PathVariable Long pricesId
    ) {
        pricePolicyService.deletePricePolicy(cafeId, pricesId);
        return ResponseEntity.ok("가격 정책이 삭제되었습니다.");
    }
}

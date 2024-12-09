package com.sparta.kidscafe.domain.pricepolicy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.kidscafe.common.enums.TargetType;
import com.sparta.kidscafe.domain.pricepolicy.dto.request.PricePolicyCreateRequestDto;
import com.sparta.kidscafe.domain.pricepolicy.service.PricePolicyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PricePolicyController.class)
@AutoConfigureMockMvc(addFilters = false)
class PricePolicyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PricePolicyService pricePolicyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("가격 정책 추가 - 성공")
    void addPricePolicySuccess() throws Exception {
        PricePolicyCreateRequestDto requestDto = PricePolicyCreateRequestDto.builder()
                .targetType(TargetType.FEE)
                .targetId(1L)
                .title("주말 추가 요금")
                .dayType("월, 화, 수, 목, 금, 토, 일")
                .rate(10.00)
                .build();

        Mockito.doNothing().when(pricePolicyService).addPricePolicy(Mockito.anyLong(), Mockito.any());

        mockMvc.perform(post("/api/cafes/1/prices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated());

        Mockito.verify(pricePolicyService, Mockito.times(1))
                .addPricePolicy(Mockito.eq(1L), Mockito.any(PricePolicyCreateRequestDto.class));
    }

    @Test
    @DisplayName("가격 정책 추가 - 실패 (유효성 검사 실패)")
    void addPricePolicyValidationFailure() throws Exception {
        PricePolicyCreateRequestDto requestDto = PricePolicyCreateRequestDto.builder()
                .targetType(null)
                .targetId(null)
                .title("")
                .dayType(null)
                .rate(null)
                .build();

        mockMvc.perform(post("/api/cafes/1/prices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());

        Mockito.verify(pricePolicyService, Mockito.never()).addPricePolicy(Mockito.anyLong(), Mockito.any());
    }
}
package com.sparta.kidscafe.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.ResponseDto;
import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.domain.user.dto.request.UserProfileUpdateRequestDto;
import com.sparta.kidscafe.domain.user.dto.response.UserProfileResponseDto;
import com.sparta.kidscafe.domain.user.service.UserProfileService;
import com.sparta.kidscafe.exception.BusinessException;
import com.sparta.kidscafe.exception.ErrorCode;
import com.sparta.kidscafe.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserProfileControllerTest {

    private MockMvc mockMvc;
    private UserProfileService userProfileService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userProfileService = Mockito.mock(UserProfileService.class);
        UserProfileController controller = new UserProfileController(userProfileService);
        objectMapper = new ObjectMapper();

        // ArgumentResolver 추가
        HandlerMethodArgumentResolver authResolver = new TestAuthUserArgumentResolver();

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(authResolver)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("프로필 조회 성공")
    void getUserProfile_Success() throws Exception {
        UserProfileResponseDto responseDto = UserProfileResponseDto.builder()
                .id(1L)
                .email("0411khj@naver.com")
                .name("김혜진")
                .nickname("호홍")
                .address("부산광역시 남구")
                .role(RoleType.USER)
                .build();

        when(userProfileService.getUserProfile(any(AuthUser.class)))
                .thenReturn(ResponseDto.success(responseDto, HttpStatus.OK, "회원 조회 성공"));

        mockMvc.perform(get("/api/users/profile")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("회원 조회 성공"))
                .andExpect(jsonPath("$.data.name").value("김혜진"))
                .andExpect(jsonPath("$.data.nickname").value("호홍"))
                .andExpect(jsonPath("$.data.address").value("부산광역시 남구"));
    }

    @Test
    @DisplayName("프로필 조회 실패 - 사용자 정보 없음")
    void getUserProfile_Fail_UserNotFound() throws Exception {
        when(userProfileService.getUserProfile(any(AuthUser.class)))
                .thenThrow(new BusinessException(ErrorCode.USER_NOT_FOUND));

        mockMvc.perform(get("/api/users/profile")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.state").value(404))
                .andExpect(jsonPath("$.message").value("사용자를 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("프로필 수정 성공")
    void updateUserProfile_Success() throws Exception {
        UserProfileUpdateRequestDto requestDto = new UserProfileUpdateRequestDto(
                "김혜진", "힝", "부산광역시 해운대구", "password123");

        UserProfileResponseDto responseDto = UserProfileResponseDto.builder()
                .id(1L)
                .email("0411khj@naver.com")
                .name("김혜진")
                .nickname("힝")
                .address("부산광역시 해운대구")
                .role(RoleType.USER)
                .build();

        when(userProfileService.updateUserProfile(any(AuthUser.class), any(UserProfileUpdateRequestDto.class)))
                .thenReturn(ResponseDto.success(responseDto, HttpStatus.OK, "프로필 수정 성공"));

        mockMvc.perform(patch("/api/users/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("프로필 수정 성공"))
                .andExpect(jsonPath("$.data.address").value("부산광역시 해운대구"))
                .andExpect(jsonPath("$.data.nickname").value("힝"));
    }

    @Test
    @DisplayName("프로필 수정 실패 - 비밀번호 형식 불일치")
    void updateUserProfile_Fail_InvalidPassword() throws Exception {
        UserProfileUpdateRequestDto requestDto = new UserProfileUpdateRequestDto(
                "김혜진", "힝", "부산광역시 해운대구", "short");

        mockMvc.perform(patch("/api/users/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.state").value(400))
                .andExpect(jsonPath("$.message").value("비밀번호는 8자 이상, 20자 이하로 입력해야 합니다."));
    }

    private static class TestAuthUserArgumentResolver implements HandlerMethodArgumentResolver {

        @Override
        public boolean supportsParameter(org.springframework.core.MethodParameter parameter) {
            return parameter.getParameterType().equals(AuthUser.class);
        }

        @Override
        public Object resolveArgument(org.springframework.core.MethodParameter parameter,
                                      org.springframework.web.method.support.ModelAndViewContainer mavContainer,
                                      org.springframework.web.context.request.NativeWebRequest webRequest,
                                      org.springframework.web.bind.support.WebDataBinderFactory binderFactory) {
            return new AuthUser(1L, "testUser", RoleType.USER);
        }
    }
}
package com.sparta.kidscafe.domain.user.controller;

import com.sparta.kidscafe.common.dto.ListResponseDto;
import com.sparta.kidscafe.common.util.JwtUtil;
import com.sparta.kidscafe.domain.user.dto.response.UserResponseDto;
import com.sparta.kidscafe.domain.user.service.UserOwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserOwnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserOwnerService userOwnerService;

    @MockBean
    private JwtUtil jwtUtil;

    @BeforeEach
    void setup() {
        when(jwtUtil.extractUserId("valid-token")).thenReturn(1L);
        when(jwtUtil.extractRoleType("valid-token")).thenReturn("OWNER");
        when(jwtUtil.extractRoleType("invalid-token"))
                .thenThrow(new IllegalArgumentException("유효하지 않은 토큰입니다."));
    }

    @Test
    @DisplayName("회원 조회 성공")
    void getUsersWhoFavoritedOwner_Success() throws Exception {
        List<UserResponseDto> users = List.of(
                new UserResponseDto(1L, "0411khj@naver.com", "김혜진", "귀염둥이", "부산광역시 남구", null, "USER"),
                new UserResponseDto(2L, "0411khj@naver.com", "김혜진", "귀염둥이", "부산광역시 해운대구", null, "USER")
        );

        when(userOwnerService.getUsersWhoFavoritedOwner(any(), anyInt(), anyInt()))
                .thenReturn(ListResponseDto.success(users, HttpStatus.OK, "회원 조회 성공"));

        mockMvc.perform(get("/api/owners/users")
                        .header("Authorization", "Bearer valid-token")
                        .param("page", "1")
                        .param("size", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("회원 조회 성공"))
                .andExpect(jsonPath("$.data[0].email").value("0411khj@naver.com"))
                .andExpect(jsonPath("$.data[0].address").value("부산광역시 남구"));
    }

    @Test
    @DisplayName("권한 없음 - 유효하지 않은 토큰")
    void getUsersWhoFavoritedOwner_NoPermission() throws Exception {
        mockMvc.perform(get("/api/owners/users")
                        .header("Authorization", "Bearer invalid-token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("유효하지 않은 토큰입니다."));
    }
}

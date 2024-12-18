package com.sparta.kidscafe.domain.user.controller;

import com.sparta.kidscafe.common.dto.ListResponseDto;
import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.common.util.JwtUtil;
import com.sparta.kidscafe.domain.user.dto.response.UserResponseDto;
import com.sparta.kidscafe.domain.user.service.UserAdminService;
import com.sparta.kidscafe.dummy.DummyUser;
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
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserAdminService userAdminService;

    @MockBean
    private JwtUtil jwtUtil;

    private List<UserResponseDto> dummyUserResponseList;

    @BeforeEach
    void setup() {
        // valid-admin-token에 대한 Mock 설정
        when(jwtUtil.extractUserId("valid-admin-token")).thenReturn(1L);
        when(jwtUtil.extractEmail("valid-admin-token")).thenReturn("admin@test.com");
        when(jwtUtil.extractRoleType("valid-admin-token")).thenReturn("ADMIN");

        // invalid-token에 대한 Mock 설정
        when(jwtUtil.extractRoleType("invalid-token"))
                .thenThrow(new IllegalArgumentException("유효하지 않은 토큰입니다."));

        // 더미 데이터 생성
        dummyUserResponseList = DummyUser.createDummyUsers(RoleType.ADMIN, 2).stream()
                .map(UserResponseDto::fromEntity)
                .collect(Collectors.toList());

        // UserAdminService Mock 설정
        when(userAdminService.getAdminUsers(anyInt(), anyInt()))
                .thenReturn(ListResponseDto.success(
                        dummyUserResponseList,
                        HttpStatus.OK,
                        "회원 조회 성공"
                ));
    }

    @Test
    @DisplayName("회원 조회 성공 - ADMIN 권한")
    void getAdminUsers_Success() throws Exception {
        mockMvc.perform(get("/api/admin/users")
                        .header("Authorization", "Bearer valid-admin-token")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("회원 조회 성공"))
                .andExpect(jsonPath("$.data[0].email").value(dummyUserResponseList.get(0).getEmail()))
                .andExpect(jsonPath("$.data[1].nickname").value(dummyUserResponseList.get(1).getNickname()));
    }

    @Test
    @DisplayName("회원 조회 실패 - 권한 없음")
    void getAdminUsers_Fail_NoPermission() throws Exception {
        mockMvc.perform(get("/api/admin/users")
                        .header("Authorization", "Bearer invalid-token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("유효하지 않은 토큰입니다."));
    }
}

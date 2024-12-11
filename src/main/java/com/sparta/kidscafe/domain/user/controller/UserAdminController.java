package com.sparta.kidscafe.domain.user.controller;

import com.sparta.kidscafe.domain.user.dto.response.UserAdminResponseDto;
import com.sparta.kidscafe.domain.user.service.UserAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserAdminController {

    private final UserAdminService userAdminService;

    @GetMapping("/api/admin/users")
    public ResponseEntity<UserAdminResponseDto> getAdminUsers(
            @RequestHeader("Authorization") String authorization,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        UserAdminResponseDto responseDto = userAdminService.getAdminUsers(page, size);
        return ResponseEntity.ok(responseDto);
    }
}
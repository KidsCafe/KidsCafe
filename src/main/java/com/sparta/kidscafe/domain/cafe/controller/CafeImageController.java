package com.sparta.kidscafe.domain.cafe.controller;

import com.sparta.kidscafe.common.annotation.Auth;
import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.domain.cafe.dto.request.CafeImageDeleteRequestDto;
import com.sparta.kidscafe.domain.cafe.service.CafeImageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CafeImageController {

  private final CafeImageService cafeImageService;

  @PostMapping("/cafes/images")
  public ResponseEntity<StatusDto> uploadCafeImage(
      @Auth AuthUser authUser,
      @RequestParam(value = "targetId", required = false) String targetId,
      @RequestPart(value = "images") List<MultipartFile> images
  ) {
    Long cafeId = StringUtils.hasText(targetId) ? Long.parseLong(targetId) : null;
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(cafeImageService.uploadCafeImage(authUser, cafeId, images));
  }

  @DeleteMapping("/cafes/images")
  public ResponseEntity<StatusDto> deleteCafeImage(
      @Auth AuthUser authUser,
      @RequestBody CafeImageDeleteRequestDto requestDto
  ) {
    cafeImageService.deleteImage(authUser, requestDto);
    return ResponseEntity.noContent().build();
  }
}

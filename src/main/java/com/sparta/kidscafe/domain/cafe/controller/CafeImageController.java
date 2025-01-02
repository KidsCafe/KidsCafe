package com.sparta.kidscafe.domain.cafe.controller;

import com.sparta.kidscafe.common.annotation.Auth;
import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.ListResponseDto;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.common.util.valid.AuthValidationCheck;
import com.sparta.kidscafe.common.util.valid.ImageValidationCheck;
import com.sparta.kidscafe.domain.cafe.dto.request.CafeImageDeleteRequestDto;
import com.sparta.kidscafe.domain.cafe.service.CafeImageService;
import com.sparta.kidscafe.domain.image.dto.ImageResponseDto;
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

  @PostMapping("/owners/cafes/images")
  public ResponseEntity<StatusDto> uploadCafeImage(
      @Auth AuthUser authUser,
      @RequestParam(value = "cafeId", required = false) String cafeId,
      @RequestPart(value = "images") List<MultipartFile> images
  ) {
    Long parseId = getCageId(cafeId);
    ImageValidationCheck.validCafeImage(images);
    List<ImageResponseDto> responseImages = cafeImageService.uploadCafeImage(authUser, parseId,
        images);
    String message = "이미지 [" + images.size() + "]장 등록 성공";
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(ListResponseDto.create(responseImages, message));
  }

  @DeleteMapping("/owners/cafes/images")
  public ResponseEntity<StatusDto> deleteCafeImage(
      @Auth AuthUser authUser,
      @RequestBody CafeImageDeleteRequestDto requestDto
  ) {
    AuthValidationCheck.validNotUser(authUser);
    cafeImageService.deleteImage(authUser, requestDto);
    return ResponseEntity.noContent().build();
  }

  public Long getCageId(String cafeId) {
    return StringUtils.hasText(cafeId) ? Long.parseLong(cafeId) : null;
  }
}

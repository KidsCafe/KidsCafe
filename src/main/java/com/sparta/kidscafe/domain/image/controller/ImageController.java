package com.sparta.kidscafe.domain.image.controller;


import com.sparta.kidscafe.common.annotation.Auth;
import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.domain.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ImageController {

  private final ImageService imageService;

  @DeleteMapping("/images")
  public ResponseEntity<?> deleteGhostImage(
      @Auth AuthUser authUser
  ) {
    imageService.deleteGhostImage(authUser);
    return ResponseEntity.noContent().build();
  }
}

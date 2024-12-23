package com.sparta.kidscafe.domain.image.service;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.util.FileStorageUtil;
import com.sparta.kidscafe.common.util.valid.AuthValidationCheck;
import com.sparta.kidscafe.domain.cafe.entity.CafeImage;
import com.sparta.kidscafe.domain.cafe.repository.CafeImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {

  private final CafeImageRepository cafeImageRepository;
  private final FileStorageUtil fileStorage;

  public void deleteGhostImage(AuthUser authUser) {
    AuthValidationCheck.validAdmin(authUser);
    List<CafeImage> images = cafeImageRepository.findAllByCafeId(null);
    for (CafeImage image : images) {
      fileStorage.deleteImage(image.getImagePath());
    }
    cafeImageRepository.deleteAll(images);
  }
}

package com.sparta.kidscafe.domain.image.service;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.util.FileUtil;
import com.sparta.kidscafe.domain.cafe.entity.CafeImage;
import com.sparta.kidscafe.domain.cafe.repository.CafeImageRepository;
import com.sparta.kidscafe.domain.cafe.service.CafeValidationCheck;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageService {

  private final CafeImageRepository cafeImageRepository;
  private final FileUtil fileUtil;

  public void deleteGhostImage(AuthUser authUser) {
    CafeValidationCheck.validAdmin(authUser);
    List<CafeImage> images = cafeImageRepository.findAllByCafeId(null);
    for (CafeImage image : images) {
      fileUtil.deleteImage(image.getImagePath());
    }
    cafeImageRepository.deleteAll(images);
  }
}

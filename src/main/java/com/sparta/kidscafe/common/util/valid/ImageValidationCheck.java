package com.sparta.kidscafe.common.util.valid;

import com.sparta.kidscafe.domain.cafe.repository.CafeImageRepository;
import com.sparta.kidscafe.exception.BusinessException;
import com.sparta.kidscafe.exception.ErrorCode;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class ImageValidationCheck {

  private final CafeImageRepository cafeImageRepository;
  private static final int MAXIMUM_COUNT = 20;
  private static final int MAXIMUM_SIZE = 500;
  private static final List<String> extensions = List.of("jpg", "jpeg", "png");

  public void cafeImageOverCount(Long cafeId, int newCount) {
    Long count = cafeImageRepository.countByCafeId(cafeId);
    if (count + newCount > MAXIMUM_COUNT) {
      throw new BusinessException(ErrorCode.IMAGE_OVER_MAXIMUM_COUNT);
    }
  }

  public static void validCafeImage(List<MultipartFile> images) {
    if (images.size() > MAXIMUM_COUNT) {
      throw new BusinessException(ErrorCode.IMAGE_OVER_MAXIMUM_COUNT);
    }

    for (MultipartFile image : images) {
      isImageExt(image);
      overSize(image);
    }
  }

  private static void isImageExt(MultipartFile image) {
    int pos = Objects.requireNonNull(image.getOriginalFilename()).lastIndexOf(".");
    String ext = image.getOriginalFilename().substring(pos + 1).toLowerCase();
    if (extensions.stream().noneMatch(item -> item.equals(ext))) {
      throw new BusinessException(ErrorCode.IMAGE_UNSUPPORTED_FORMAT);
    }
  }

  private static void overSize(MultipartFile image) {
    if (image.getSize() > MAXIMUM_SIZE) {
      throw new BusinessException(ErrorCode.IMAGE_OVER_MAXIMUM_SIZE);
    }
  }
}

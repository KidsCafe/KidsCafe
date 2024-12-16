package com.sparta.kidscafe.common.util;

import com.sparta.kidscafe.common.enums.ImageType;
import org.springframework.web.multipart.MultipartFile;

public interface ImageUtil {
  String makeDirectory(String dirPath, ImageType imageType, Long id);

  String makeFileName(String dirPath, MultipartFile image);

  void uploadImage(String uploadPath, MultipartFile image);

  void deleteImage(String deleteImagePath);
}

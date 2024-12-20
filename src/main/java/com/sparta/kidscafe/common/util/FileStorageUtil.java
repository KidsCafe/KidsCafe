package com.sparta.kidscafe.common.util;

import com.sparta.kidscafe.common.enums.ImageType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageUtil {

  String uploadImage(String uploadPath, MultipartFile image);

  void deleteImage(String deleteImagePath);

  String makeDirectory(ImageType imageType, Long id);

  default String makeFileName(String dirPath, MultipartFile image) {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
    String formattedDateTime = now.format(formatter);

    String imagePath = dirPath + formattedDateTime
        + "_"
        + image.getOriginalFilename();
    return imagePath;
  }
}

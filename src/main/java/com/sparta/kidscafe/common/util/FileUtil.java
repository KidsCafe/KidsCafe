package com.sparta.kidscafe.common.util;

import com.sparta.kidscafe.exception.BusinessException;
import com.sparta.kidscafe.exception.ErrorCode;
import java.io.File;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * S3대신 임시로 테스트 할 업로드용 클래스
 */
@Slf4j(topic = "FileUtil")
@Component
@RequiredArgsConstructor
public class FileUtil {

  public String makeDirectory(String dirPath, Long id) {
    StringBuilder dirDetailPath = new StringBuilder(dirPath);
    dirDetailPath.append(id);
    dirDetailPath.append("/");
    File directory = new File(dirDetailPath.toString());
    if (!directory.exists()) {
      directory.mkdirs();
    }
    return dirDetailPath.toString();
  }

  public String makeFileName(String dirPath, Long id, MultipartFile image) {
    StringBuilder imagePath = new StringBuilder(dirPath);
    imagePath.append("cafeImage");
    imagePath.append("_");
    imagePath.append(id);
    imagePath.append("_");
    imagePath.append(image.getOriginalFilename());
    return imagePath.toString();
  }

  public void uploadImage(String uploadPath, MultipartFile image) {
    try {
      image.transferTo(new File(uploadPath));
    } catch (IOException ex) {
      ex.printStackTrace();
      throw new BusinessException(ErrorCode.CAFE_IMAGE_UPLOAD_FAILED);
    }
  }

  public void deleteImage(String deleteImagePath) {
    File file = new File(deleteImagePath);
    if (file.exists()) {
      if (!file.delete()) {
        throw new BusinessException(ErrorCode.IMAGE_REMOVE_FAILED);
      }
    } else {
      throw new BusinessException(ErrorCode.IMAGE_NOT_EXIST);
    }
  }

  public void updateImage(String oriImagePath, String newImagePath, MultipartFile image) {
    deleteImage(oriImagePath);
    uploadImage(newImagePath, image);
  }
}

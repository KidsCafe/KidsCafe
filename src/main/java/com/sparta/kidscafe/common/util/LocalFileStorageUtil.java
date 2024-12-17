package com.sparta.kidscafe.common.util;

import com.sparta.kidscafe.common.enums.ImageType;
import com.sparta.kidscafe.exception.BusinessException;
import com.sparta.kidscafe.exception.ErrorCode;
import java.io.File;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

/**
 * S3대신 임시로 테스트 할 업로드용 클래스
 */
@Slf4j(topic = "FileUtil")
@RequiredArgsConstructor
public class LocalFileStorageUtil implements FileStorageUtil {

  @Value("${filePath.cafe}")
  private String defaultImagePath;

  @Override
  public String makeDirectory(ImageType imageType, Long id) {
    StringBuilder dirDetailPath = new StringBuilder(defaultImagePath);
    dirDetailPath.append(id);
    dirDetailPath.append("/");
    dirDetailPath.append(imageType.toString().toLowerCase());
    dirDetailPath.append("/");
    File directory = new File(dirDetailPath.toString());
    if (!directory.exists()) {
      directory.mkdirs();
    }
    return dirDetailPath.toString();
  }

  @Override
  public String uploadImage(String uploadPath, MultipartFile image) {
    try {
      image.transferTo(new File(uploadPath));
      return uploadPath;
    } catch (IOException ex) {
      ex.printStackTrace();
      throw new BusinessException(ErrorCode.IMAGE_UPLOAD_FAILED);
    }
  }

  @Override
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
}

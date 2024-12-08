package com.sparta.kidscafe.common.util;

import com.sparta.kidscafe.exception.BusinessException;
import com.sparta.kidscafe.exception.ErrorCode;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * S3대신 임시로 테스트 할 업로드용 클래스
 */
@Slf4j(topic = "FileUtil")
@Component
@RequiredArgsConstructor
public class FileUtil {

  @Value("${filePath.cafe}")
  private String defaultImagePath;

  public String uploadCafeImage(MultipartFile image, Long cafeId) throws IOException {
    StringBuilder imagePath = new StringBuilder(defaultImagePath);
    imagePath.append(cafeId);
    imagePath.append("/");
    File directory = new File(imagePath.toString());
    if (!directory.exists()) {
      directory.mkdirs();
    }

    imagePath.append(cafeId);
    imagePath.append("_");
    imagePath.append(image.getOriginalFilename());
    image.transferTo(new File(imagePath.toString()));
    return imagePath.toString();
  }

  public List<String> uploadCafeImage(List<MultipartFile> images, Long cafeId) {
    List<String> imagePaths = new ArrayList<>();
    try {
      for(MultipartFile image: images)
        imagePaths.add(uploadCafeImage(image, cafeId));
      return imagePaths;
    } catch (IOException ex) {
      ex.printStackTrace();
      throw new BusinessException(ErrorCode.CAFE_IMAGE_UPLOAD_FAILED);
    }
  }
}

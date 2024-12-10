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

  public String uploadCafeImage(Long cafeId, MultipartFile image) {
    try {
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
    } catch (IOException ex) {
      ex.printStackTrace();
      throw new BusinessException(ErrorCode.CAFE_IMAGE_UPLOAD_FAILED);
    }
  }

  public List<String> uploadCafeImage(Long cafeId, List<MultipartFile> images) {
    List<String> imagePaths = new ArrayList<>();
    try {
      StringBuilder dirPath = new StringBuilder(defaultImagePath);
      dirPath.append(cafeId);
      dirPath.append("/");
      File directory = new File(dirPath.toString());
      if (!directory.exists()) {
        directory.mkdirs();
      }

      for (MultipartFile image : images) {
        StringBuilder imagePath = new StringBuilder(dirPath.toString());
        imagePath.append(cafeId);
        imagePath.append("_");
        imagePath.append(image.getOriginalFilename());
        image.transferTo(new File(imagePath.toString()));
        imagePaths.add(imagePath.toString());
      }

      return imagePaths;
    } catch (IOException ex) {
      ex.printStackTrace();
      throw new BusinessException(ErrorCode.CAFE_IMAGE_UPLOAD_FAILED);
    }
  }

  public boolean deleteFile(String deleteImagePath) {
    File file = new File(deleteImagePath);
    if (file.exists()) {
      return file.delete(); // 삭제 성공 시 true 반환
    } else {
      throw new BusinessException(ErrorCode.CAFE_IMAGE_NOT_EXIST);
    }
  }

  public String updateCafeImage(Long cafeId, String oriImagePath, MultipartFile image) {
    deleteFile(oriImagePath);
    return uploadCafeImage(cafeId, image);
  }
}

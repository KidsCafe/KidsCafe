package com.sparta.kidscafe.common.util;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.sparta.kidscafe.common.enums.ImageType;
import com.sparta.kidscafe.exception.BusinessException;
import com.sparta.kidscafe.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
public class S3FileStorageUtil implements FileStorageUtil {

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;
  private final AmazonS3 amazonS3;

  @Override
  public String makeDirectory(ImageType imageType, Long id) {
    String dirPath = id
        + "/"
        + imageType.toString().toLowerCase()
        + "/";
    return dirPath;
  }

  @Override
  public String uploadImage(String uploadPath, MultipartFile image) {
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(image.getSize());
    metadata.setContentType(image.getContentType());

    try {
      amazonS3.putObject(bucket, uploadPath, image.getInputStream(), metadata);
    } catch (AmazonS3Exception e) {
      throw new BusinessException(ErrorCode.IMAGE_S3_UPLOAD_FAILED);
    } catch (SdkClientException e) {
      throw new BusinessException(ErrorCode.IMAGE_SDK_ERROR);
    } catch (Exception e) {
      throw new BusinessException(ErrorCode.IMAGE_UPLOAD_FAILED);
    }

    return amazonS3.getUrl(bucket, uploadPath).toString();
  }

  @Override
  public void deleteImage(String deleteImagePath) {
    try {
      boolean isObjectExist = amazonS3.doesObjectExist(bucket, deleteImagePath);
      if (isObjectExist) {
        amazonS3.deleteObject(bucket, deleteImagePath);
      }
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }
}
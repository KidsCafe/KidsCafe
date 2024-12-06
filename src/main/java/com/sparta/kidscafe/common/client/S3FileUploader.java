package com.sparta.kidscafe.common.client;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

@Configuration
public class S3FileUploader {

  private final AmazonS3Client amazonS3Client;
  private final String bucket;


  public S3FileUploader(
      AmazonS3Client amazonS3Client,
      @Value("${cloud.aws.s3.bucket}") String bucket
  ) {
    this.bucket = bucket;
    this.amazonS3Client = amazonS3Client;
  }

  public List<String> uploadFiles(List<MultipartFile> files) {
    List<String> fileUrls = new ArrayList<>();

    try {
      for (MultipartFile file : files) {
        String fileName = file.getOriginalFilename();
        String fileUrl = "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + fileName;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);

        fileUrls.add(fileUrl);
      }
      return fileUrls;
    } catch (IOException e) {
      throw new IllegalArgumentException("FILE_UPLOAD_FAILED");
    }
  }
}
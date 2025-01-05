package com.sparta.kidscafe.common.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.sparta.kidscafe.common.util.FileStorageUtil;
import com.sparta.kidscafe.common.util.LocalFileStorageUtil;
import com.sparta.kidscafe.common.util.S3FileStorageUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class FileStorageConfig {
  @Bean
  @Profile("temp")
  public FileStorageUtil localFileStorageService() {
    return new LocalFileStorageUtil();
  }

  @Bean
  @Profile({"dev", "prod"})
  public FileStorageUtil s3FileStorageService(
      @Value("${cloud.aws.credentials.accessKey}") String accessKey,
      @Value("${cloud.aws.credentials.secretKey}") String secretKey,
      @Value("${cloud.aws.region.static}") String region
  ) {
    AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
    AmazonS3 build = AmazonS3ClientBuilder
        .standard()
        .withRegion(region)
        .withCredentials(new AWSStaticCredentialsProvider(credentials))
        .build();
    return new S3FileStorageUtil(build);
  }
}

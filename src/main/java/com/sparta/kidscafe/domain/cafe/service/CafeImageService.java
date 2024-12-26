package com.sparta.kidscafe.domain.cafe.service;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.enums.ImageType;
import com.sparta.kidscafe.common.util.FileStorageUtil;
import com.sparta.kidscafe.common.util.valid.CafeValidationCheck;
import com.sparta.kidscafe.domain.cafe.dto.request.CafeImageDeleteRequestDto;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.entity.CafeImage;
import com.sparta.kidscafe.domain.cafe.repository.CafeImageRepository;
import com.sparta.kidscafe.domain.image.dto.ImageResponseDto;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CafeImageService {

  private final CafeValidationCheck cafeValidationCheck;
  private final CafeImageRepository cafeImageRepository;
  private final FileStorageUtil fileStorage;

  @Transactional
  public List<ImageResponseDto> uploadCafeImage(AuthUser authUser, Long cafeId, List<MultipartFile> images) {
    // Cafe 객체 검증 및 조회
    Cafe cafe = cafeValidationCheck.validMyCafe(cafeId, authUser.getId());
    return uploadCafeImages(cafe, images);
  }

  @Transactional
  public void deleteImage(AuthUser authUser, CafeImageDeleteRequestDto requestDto) {
    cafeValidationCheck.validMyCafe(requestDto.getCafeId(), authUser.getId());
    List<CafeImage> deleteImages = cafeImageRepository.findAllById(requestDto.getImages());
    for (CafeImage deleteImage : deleteImages) {
      deleteImage.delete();
    }
  }

  private List<ImageResponseDto> uploadCafeImages(Cafe cafe, List<MultipartFile> images) {
    List<ImageResponseDto> responseImages = new ArrayList<>();
    for (MultipartFile image : images) {
      String imagePath = uploadCafeImage(cafe.getUser().getId(), image); // 이미지 업로드
      CafeImage cafeImage = saveCafeImage(imagePath, cafe); // Cafe 객체로 저장
      ImageResponseDto responseDto = ImageResponseDto.builder()
          .id(cafeImage.getId())
          .imagePath(imagePath)
          .build();
      responseImages.add(responseDto);
    }
    return responseImages;
  }

  private String uploadCafeImage(Long userId, MultipartFile image) {
    String dirPath = fileStorage.makeDirectory(ImageType.CAFE, userId);
    String imagePath = fileStorage.makeFileName(dirPath, image);
    return fileStorage.uploadImage(imagePath, image);
  }

  private CafeImage saveCafeImage(String imagePath, Cafe cafe) {
    CafeImage cafeImage = new CafeImage(cafe, imagePath);
    cafeImageRepository.save(cafeImage);
    return cafeImage;
  }
}

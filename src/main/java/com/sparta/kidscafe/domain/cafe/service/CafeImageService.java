package com.sparta.kidscafe.domain.cafe.service;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.ListResponseDto;
import com.sparta.kidscafe.common.enums.ImageType;
import com.sparta.kidscafe.common.util.FileStorageUtil;
import com.sparta.kidscafe.common.util.valid.CafeValidationCheck;
import com.sparta.kidscafe.domain.cafe.dto.request.CafeImageDeleteRequestDto;
import com.sparta.kidscafe.domain.cafe.entity.CafeImage;
import com.sparta.kidscafe.domain.cafe.repository.CafeImageRepository;
import com.sparta.kidscafe.domain.image.dto.ImageResponseDto;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
  public ListResponseDto<ImageResponseDto> uploadCafeImage(AuthUser authUser,
      Long cafeId, List<MultipartFile> images) {
    List<ImageResponseDto> responseImages = uploadCafeImage(authUser.getId(), cafeId, images);
    return ListResponseDto.success(
        responseImages,
        HttpStatus.CREATED,
        "이미지 [" + images.size() + "]장 등록 성공");
  }

  @Transactional
  public void deleteImage(AuthUser authUser, CafeImageDeleteRequestDto requestDto) {
    cafeValidationCheck.validMyCafe(requestDto.getCafeId(), authUser.getId());
    List<CafeImage> deleteImages = cafeImageRepository.findAllById(requestDto.getImages());
    for (CafeImage deleteImage : deleteImages) {
      deleteImage.delete();
    }
  }

  private List<ImageResponseDto> uploadCafeImage(Long userId, Long cafeId,
      List<MultipartFile> images) {
    List<ImageResponseDto> responseImages = new ArrayList<>();
    for (MultipartFile image : images) {
      ImageResponseDto imageResponseDto = uploadCafeImage(userId, image);
      saveCafeImage(imageResponseDto.getImagePath(), cafeId);
      responseImages.add(imageResponseDto);
    }
    return responseImages;
  }

  private ImageResponseDto uploadCafeImage(Long userId, MultipartFile image) {
    String dirPath = fileStorage.makeDirectory(ImageType.CAFE, userId);
    String imagePath = fileStorage.makeFileName(dirPath, image);
    imagePath = fileStorage.uploadImage(imagePath, image);
    return new ImageResponseDto(imagePath);
  }

  private void saveCafeImage(String imagePath, Long cafeId) {
    CafeImage cafeImage = CafeImage.builder()
        .cafeId(cafeId)
        .imagePath(imagePath)
        .build();
    cafeImageRepository.save(cafeImage);
  }
}

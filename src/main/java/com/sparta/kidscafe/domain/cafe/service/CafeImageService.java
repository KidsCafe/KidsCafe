package com.sparta.kidscafe.domain.cafe.service;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.ListResponseDto;
import com.sparta.kidscafe.common.enums.ImageType;
import com.sparta.kidscafe.common.util.ValidationCheck;
import com.sparta.kidscafe.common.util.FileUtil;
import com.sparta.kidscafe.domain.cafe.dto.request.CafeImageDeleteRequestDto;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.entity.CafeImage;
import com.sparta.kidscafe.domain.cafe.repository.CafeImageRepository;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.domain.image.dto.ImageResponseDto;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CafeImageService {

  @Value("${filePath.cafe}")
  private String defaultImagePath;
  private final CafeRepository cafeRepository;
  private final CafeImageRepository cafeImageRepository;
  private final FileUtil fileUtil;

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
    Long cafeId = requestDto.getCafeId();
    Long userId = authUser.getId();
    Cafe cafe = cafeRepository.findByIdAndUserId(cafeId, userId).orElse(null);
    ValidationCheck.validMyCafe(authUser, cafe);

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
    String dirPath = fileUtil.makeDirectory(defaultImagePath, ImageType.CAFE, userId);
    String imagePath = fileUtil.makeFileName(dirPath, image);
    fileUtil.uploadImage(imagePath, image);
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

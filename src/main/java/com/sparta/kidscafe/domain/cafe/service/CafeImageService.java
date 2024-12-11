package com.sparta.kidscafe.domain.cafe.service;

import com.sparta.kidscafe.common.util.FileUtil;
import com.sparta.kidscafe.domain.cafe.dto.request.CafeImageRequestDto;
import com.sparta.kidscafe.domain.cafe.dto.request.CafeModifyRequestDto;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.entity.CafeImage;
import com.sparta.kidscafe.domain.cafe.repository.CafeImageRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CafeImageService {

  @Value("${filePath.cafe}")
  private String defaultImagePath;

  private final CafeImageRepository cafeImageRepository;
  private final FileUtil fileUtil;

  public List<CafeImage> searchCafeImage(Long cafeId) {
    return cafeImageRepository.findAllByCafeId(cafeId);
  }

  public void updateCafeImage(Cafe cafe, List<MultipartFile> newCafeImageFiles,
      CafeModifyRequestDto requestDto) {
    List<CafeImage> cafeImages = cafe.getCafeImages();
    for (CafeImageRequestDto cafeImageRequestDto : requestDto.getImages()) {
      if (cafeImageRequestDto.isCreate()) {
        MultipartFile newImageFile = cafeImageRequestDto.getMatchImageByFile(newCafeImageFiles);
        saveCafeImage(cafe, newImageFile);
      } else if (cafeImageRequestDto.isUpdate()) {
        MultipartFile newImageFile = cafeImageRequestDto.getMatchImageByFile(newCafeImageFiles);
        CafeImage oriImage = cafeImageRequestDto.getMatchImageByEntity(cafeImages);
        updateCafeImage(cafe, oriImage, newImageFile);
      } else {
        CafeImage deleteImage = cafeImageRequestDto.getMatchImageByEntity(cafeImages);
        deleteCafeImage(cafeImages, deleteImage);
      }
    }
  }

  public void saveCafeImages(Cafe cafe, List<MultipartFile> images) {
    for (MultipartFile image : images) {
      saveCafeImage(cafe, image);
    }
  }

  private void saveCafeImage(Cafe cafe, MultipartFile image) {
    String dirPath = fileUtil.makeDirectory(defaultImagePath, cafe.getId());
    String imagePath = fileUtil.makeFileName(dirPath, cafe.getId(), image);
    CafeImage cafeImage = CafeImage.builder()
        .cafe(cafe)
        .imagePath(imagePath)
        .build();
    cafeImageRepository.save(cafeImage);
    fileUtil.uploadImage(imagePath, image);
  }

  private void updateCafeImage(Cafe cafe, CafeImage oriImage, MultipartFile updateImage) {
    String dirPath = fileUtil.makeDirectory(defaultImagePath, cafe.getId());
    String newImagePath = fileUtil.makeFileName(dirPath, cafe.getId(), updateImage);
    String oriImagePath = oriImage.getImagePath();
    oriImage.update(newImagePath);
    fileUtil.updateImage(oriImagePath, newImagePath, updateImage);
  }

  private void deleteCafeImage(List<CafeImage> cafeImages, CafeImage deleteImage) {
    cafeImages.remove(deleteImage);
    fileUtil.deleteImage(deleteImage.getImagePath());
  }
}

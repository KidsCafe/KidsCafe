package com.sparta.kidscafe.domain.cafe.service;

import com.sparta.kidscafe.common.util.FileUtil;
import com.sparta.kidscafe.domain.cafe.dto.request.CafeImageRequestDto;
import com.sparta.kidscafe.domain.cafe.dto.request.CafeModifyRequestDto;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.entity.CafeImage;
import com.sparta.kidscafe.domain.cafe.repository.CafeImageRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CafeImageService {
  private final CafeImageRepository cafeImageRepository;
  private final FileUtil fileUtil;

  public void updateCafeImage(Cafe cafe, List<MultipartFile> newCafeImages,
      CafeModifyRequestDto requestDto) {
    Long cafeId = cafe.getId();
    List<CafeImage> cafeImages = cafe.getCafeImages();

    for (CafeImageRequestDto cafeImage : requestDto.getImages()) {
      if (cafeImage.isCreated()) {
        MultipartFile matchImage = cafeImage.getMatchImageByMultipartFile(newCafeImages);
        String imagePath = fileUtil.uploadCafeImage(cafeId, matchImage);
        cafeImageRepository.save(cafeImage.convertDtoToEntity(cafe, imagePath));
      } else if (cafeImage.isUpdated()) {
        MultipartFile matchMultiPartImage = cafeImage.getMatchImageByMultipartFile(newCafeImages);
        CafeImage matchCafeImage = cafeImage.getMatchImageFromCafeImage(cafeImages);
        String newImagePath = fileUtil.updateCafeImage(cafeId,
            matchCafeImage.getImagePath(), matchMultiPartImage);
        matchCafeImage.update(newImagePath);
      } else {
        CafeImage matchImage = cafeImage.getMatchImageFromCafeImage(cafeImages);
        cafeImageRepository.delete(matchImage);
      }
    }
  }

  public void saveCafeImages(Cafe cafe, List<MultipartFile> cafeImages) {
    List<String> imagePaths = fileUtil.uploadCafeImage(cafe.getId(), cafeImages);
    List<CafeImage> saveImages = new ArrayList<>();
    for (String imagePath : imagePaths) {
      CafeImage cafeImage = CafeImage.builder()
          .cafe(cafe)
          .imagePath(imagePath)
          .build();
      saveImages.add(cafeImage);
    }
    cafeImageRepository.saveAll(saveImages);
  }

  public List<CafeImage> searchCafeImage(Long cafeId) {
    return cafeImageRepository.findAllByCafeId(cafeId);
  }
}

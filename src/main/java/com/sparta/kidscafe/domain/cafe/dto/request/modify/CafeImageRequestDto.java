package com.sparta.kidscafe.domain.cafe.dto.request.modify;

import com.sparta.kidscafe.domain.cafe.entity.CafeImage;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CafeImageRequestDto {

  private Long id; // 기존 이미지 id
  private String newImagePath; // 업로드할 이미지 path

  public boolean isCreate() {
    return id == null;
  }

  public boolean isUpdate() {
    if(id == null)
      return false;

    return StringUtils.hasText(newImagePath);
  }

  public MultipartFile getMatchImageByFile(List<MultipartFile> images) {
    for(MultipartFile image : images) {
      int lastIndex = newImagePath.lastIndexOf("\\");
      String fileName = newImagePath.substring(lastIndex + 1);
      if(fileName.equals(image.getOriginalFilename())) {
        return image;
      }
    }

    return null;
  }

  public CafeImage getMatchImageByEntity(List<CafeImage> images) {
    for(CafeImage image : images) {
      if(id.equals(image.getId())) {
        return image;
      }
    }

    return null;
  }
}

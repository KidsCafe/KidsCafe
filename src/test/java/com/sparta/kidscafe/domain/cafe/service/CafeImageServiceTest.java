package com.sparta.kidscafe.domain.cafe.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.enums.ImageType;
import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.common.util.FileStorageUtil;
import com.sparta.kidscafe.common.util.valid.CafeValidationCheck;
import com.sparta.kidscafe.domain.cafe.dto.request.CafeImageDeleteRequestDto;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.entity.CafeImage;
import com.sparta.kidscafe.domain.cafe.repository.CafeImageRepository;
import com.sparta.kidscafe.domain.image.dto.ImageResponseDto;
import com.sparta.kidscafe.domain.user.entity.User;
import com.sparta.kidscafe.dummy.DummyCafe;
import com.sparta.kidscafe.dummy.DummyCafeImage;
import com.sparta.kidscafe.dummy.DummyUser;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

public class CafeImageServiceTest {

  @InjectMocks
  private CafeImageService cafeImageService;

  @Mock
  private CafeValidationCheck cafeValidationCheck;

  @Mock
  private CafeImageRepository cafeImageRepository;

  @Mock
  private FileStorageUtil fileUtil;

  private final String dirPath = "https://sparta.com/mock/images";

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  private AuthUser createAuthUser() {
    return new AuthUser(1L, "hong@email.com", RoleType.OWNER);
  }

  @Test
  @DisplayName("카페 이미지 업로드 성공")
  void uploadCafeImage_Success() throws IOException {
    // given - user
    Long cafeId = 1L;
    AuthUser authUser = createAuthUser();
    User user = DummyUser.createDummyUser(authUser.getRoleType());
    Cafe cafe = DummyCafe.createDummyCafe(user, cafeId);

    // given - image
    String imagePath = dirPath + "image.jpg";
    MultipartFile mockImage = mock(MultipartFile.class);
    List<MultipartFile> images = List.of(mockImage);
    CafeImage cafeImage = DummyCafeImage.createDummyCafeImage(cafe, imagePath);

    when(fileUtil.makeDirectory(ImageType.CAFE, authUser.getId())).thenReturn(dirPath);
    when(fileUtil.makeFileName(dirPath, mockImage)).thenReturn(imagePath);
    doNothing().when(mockImage).transferTo(any(File.class));
    when(cafeImageRepository.save(cafeImage)).thenReturn(cafeImage);

    // when
    List<ImageResponseDto> result = cafeImageService.uploadCafeImage(authUser, cafeId, images);

    // then
    assertEquals(1, result.size());
    verify(fileUtil, times(1)).makeDirectory(ImageType.CAFE, authUser.getId());
    verify(fileUtil, times(1)).makeFileName(dirPath, mockImage);
    verify(fileUtil, times(1)).uploadImage(imagePath, mockImage);
    verify(cafeImageRepository, times(1)).save(any());
  }

  @Test
  @DisplayName("카페 이미지 삭제 성공 (soft-delete)")
  void deleteImage_Success() {
    // given - user
    Long cafeId = 1L;
    AuthUser authUser = createAuthUser();
    User user = DummyUser.createDummyUser(authUser.getRoleType());
    Cafe cafe = DummyCafe.createDummyCafe(user, cafeId);

    // given - image
    List<Long> imageIds = List.of(1L, 2L);
    List<CafeImage> cafeImages = DummyCafeImage.createDummyCafeImages(cafe, imageIds);
    CafeImageDeleteRequestDto requestDto = new CafeImageDeleteRequestDto(cafeId, imageIds);

    when(cafeValidationCheck.validMyCafe(cafeId, authUser.getId())).thenReturn(cafe);
    when(cafeImageRepository.findAllById(imageIds)).thenReturn(cafeImages);

    // when
    cafeImageService.deleteImage(authUser, requestDto);

    // then
    verify(cafeValidationCheck).validMyCafe(cafeId, authUser.getId());
    verify(cafeImageRepository).findAllById(imageIds);
    assertNull(cafeImages.get(0).getCafe());
    assertNull(cafeImages.get(1).getCafe());
  }
}

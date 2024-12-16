package com.sparta.kidscafe.domain.image;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.common.util.FileUtil;
import com.sparta.kidscafe.domain.cafe.entity.CafeImage;
import com.sparta.kidscafe.domain.cafe.repository.CafeImageRepository;
import com.sparta.kidscafe.domain.image.service.ImageService;
import com.sparta.kidscafe.dummy.DummyCafeImage;
import com.sparta.kidscafe.exception.BusinessException;
import com.sparta.kidscafe.exception.ErrorCode;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ImageServiceTest {

  @InjectMocks
  private ImageService imageService;

  @Mock
  private CafeImageRepository cafeImageRepository;

  @Mock
  private FileUtil fileUtil;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  private AuthUser createAuthUser(RoleType role) {
    return new AuthUser(1L, "hong@email.com", role);
  }

  @Test
  @DisplayName("유령 이미지 삭제 성공 - 관리자")
  void deleteGhostImage_Success() {
    // given
    AuthUser authUser = createAuthUser(RoleType.ADMIN);
    List<CafeImage> images = DummyCafeImage.createDummyGhostImages(3);
    doNothing().when(fileUtil).deleteImage(anyString());
    when(cafeImageRepository.findAllByCafeId(null)).thenReturn(images);
    doNothing().when(cafeImageRepository).deleteAll(images);

    // when
    imageService.deleteGhostImage(authUser);

    // then
    verify(cafeImageRepository).findAllByCafeId(null);
    verify(fileUtil, times(3)).deleteImage(anyString());
    verify(cafeImageRepository).deleteAll(images);
  }

  @Test
  @DisplayName("유령 이미지 삭제 성공 - 권한이 없음")
  void deleteGhostImage_Unauthorized() {
    // given
    AuthUser authUser = createAuthUser(RoleType.USER);

    // when
    BusinessException exception = assertThrows(BusinessException.class, () ->
        imageService.deleteGhostImage(authUser)
    );

    // then
    assertEquals(
        ErrorCode.FORBIDDEN.getMessage(),
        exception.getMessage()
    );
  }
}

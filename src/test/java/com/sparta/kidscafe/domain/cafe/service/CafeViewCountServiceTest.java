package com.sparta.kidscafe.domain.cafe.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.entity.CafeViewCount;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.domain.cafe.repository.CafeViewCountRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CafeViewCountServiceTest {

  @InjectMocks
  private CafeViewCountService cafeViewCountService;

  @Mock
  private CafeRepository cafeRepository;

  @Mock
  private CafeViewCountRepository cafeViewCountRepository;

  @BeforeEach
  void setUp() {
    Mockito.reset(cafeRepository, cafeViewCountRepository);
  }
  @Test
  @DisplayName("카페 조회 시 조회수 1 증가")
  void incrementViewCount_ShouldIncrease_WhenCafeExists() {
    // Given
    Long cafeId = 1L;
    Cafe cafe = Cafe.builder()
        .id(cafeId)
        .name("Cafe A")
        .build();
    CafeViewCount cafeViewCount = CafeViewCount.builder()
        .cafe(cafe)
        .viewCount(5)
        .build();

    // Mocking
    when(cafeRepository.findById(cafeId)).thenReturn(Optional.of(cafe));
    when(cafeViewCountRepository.findByCafe(cafe)).thenReturn(Optional.of(cafeViewCount));

    // When
    int updatedViewCount = cafeViewCountService.incrementViewCount(cafeId);

    // Then
    assertEquals(6, updatedViewCount);
    verify(cafeRepository, times(1)).findById(cafeId);
    verify(cafeViewCountRepository, times(1)).save(cafeViewCount);
  }

  @Test
  @DisplayName("카페 조회수가 없을 경우 새로 생성")
  void incrementViewCount_ShouldCreateNewViewCount_WhenNoViewCountExists() {
    // Given
    Long cafeId = 1L;
    Cafe cafe = Cafe.builder()
        .id(cafeId)
        .name("Cafe B")
        .build();
    CafeViewCount newCafeViewCount = new CafeViewCount(cafe, 0);
    // When
    when(cafeRepository.findById(cafeId)).thenReturn(Optional.of(cafe));
    when(cafeViewCountRepository.findByCafe(cafe)).thenReturn(Optional.empty());
    when(cafeViewCountRepository.save(any(CafeViewCount.class))).thenReturn(newCafeViewCount);

    int updatedViewCount = cafeViewCountService.incrementViewCount(cafeId);

    // Then
    assertThat(updatedViewCount).isEqualTo(1);
    verify(cafeRepository).findById(cafeId);
    verify(cafeViewCountRepository).findByCafe(cafe);
    verify(cafeViewCountRepository).save(any(CafeViewCount.class));
  }
}
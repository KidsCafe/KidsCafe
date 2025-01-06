package com.sparta.kidscafe.domain.cafe.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.kidscafe.common.dto.ListResponseDto;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.domain.cafe.dto.response.CafeRankingResponseDto;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.entity.CafeViewCount;
import com.sparta.kidscafe.domain.cafe.repository.CafeViewCountRepository;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

@ExtendWith(MockitoExtension.class)
class CafeRankingServiceTest {

  @InjectMocks
  private CafeRankingService cafeRankingService;

  @Mock
  private CafeViewCountRepository cafeViewCountRepository;

  @Mock
  private RedisTemplate<String, Object> redisTemplate;

  @Mock
  private ZSetOperations<String, Object> zSetOperations;

  @BeforeEach
  void setUp() {
    lenient().when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);
  }

  @Test
  @DisplayName("DB 조회 후 Redis에 인기 카페 갱신 성공")
  void testRefreshTopN_Success() {
    // Given
    String region = "Seoul";
    int n = 3;
    List<CafeViewCount> mockCafes = List.of(
        new CafeViewCount(new Cafe(1L, "Cafe A", "Seoul"), 100),
        new CafeViewCount(new Cafe(2L, "Cafe B", "Seoul"), 80),
        new CafeViewCount(new Cafe(3L, "Cafe C", "Seoul"), 60)
    );
    Page<CafeViewCount> mockPage = new PageImpl<>(mockCafes);

    when(cafeViewCountRepository.findCafesByRegionOrderByViewCountDesc(eq(region), any(PageRequest.class)))
        .thenReturn(mockPage);

    // When
    StatusDto result = cafeRankingService.refreshTopN(region, n);

    // Then
    assertNotNull(result);
    assertEquals(200, result.getStatus());
    assertEquals(region + " 지역 인기 카페 갱신 성공", result.getMessage());

    verify(zSetOperations, times(1)).removeRange("popular-ranking:" + region, 0, -1);
    verify(zSetOperations, times(mockCafes.size())).add(anyString(), any(CafeRankingResponseDto.class), anyDouble());
  }

  @Test
  @DisplayName("DB 조회 시 데이터가 없을 경우 갱신 실패")
  void testRefreshTopN_NoContent() {
    // Given
    String region = "Seoul";
    int n = 3;
    Page<CafeViewCount> mockPage = new PageImpl<>(Collections.emptyList());

    when(cafeViewCountRepository.findCafesByRegionOrderByViewCountDesc(eq(region), any(PageRequest.class)))
        .thenReturn(mockPage);

    // When
    StatusDto result = cafeRankingService.refreshTopN(region, n);

    // Then
    assertNotNull(result);
    assertEquals(204, result.getStatus());
    assertEquals("해당 지역(" + region + ")에 데이터가 없습니다.", result.getMessage());

    verify(zSetOperations, never()).add(anyString(), any(), anyDouble());
  }

  @Test
  @DisplayName("Redis에서 지역별 인기 카페 Top 10 조회 성공")
  void testGetTopN_Success() {
    // Given
    String region = "Seoul";
    int n = 3;
    String redisKey = "popular-ranking:" + region;

    Set<ZSetOperations.TypedTuple<Object>> mockSet = new HashSet<>();
    mockSet.add(new MockTypedTuple<>(new CafeRankingResponseDto(1L, "Cafe A", 0), 100.0));
    mockSet.add(new MockTypedTuple<>(new CafeRankingResponseDto(2L, "Cafe B", 0), 80.0));
    mockSet.add(new MockTypedTuple<>(new CafeRankingResponseDto(3L, "Cafe C", 0), 60.0));

    when(redisTemplate.opsForZSet().reverseRangeWithScores(redisKey, 0, n - 1)).thenReturn(mockSet);

    // When
    ListResponseDto<CafeRankingResponseDto> result = cafeRankingService.getTopN(region, n);

    // Then
    assertNotNull(result);
    assertEquals(3, result.getData().size());
    assertEquals("Seoul 지역 인기 카페 조회 성공", result.getMessage());

    verify(redisTemplate.opsForZSet(), times(1)).reverseRangeWithScores(redisKey, 0, n - 1);
  }

  // MockTypedTuple class to simulate Redis TypedTuple
  static class MockTypedTuple<V> implements ZSetOperations.TypedTuple<V> {
    private final V value;
    private final Double score;

    public MockTypedTuple(V value, Double score) {
      this.value = value;
      this.score = score;
    }

    @Override
    public V getValue() {
      return value;
    }

    @Override
    public Double getScore() {
      return score;
    }

    @Override
    public int compareTo(ZSetOperations.TypedTuple<V> o) {
      return Double.compare(this.score, o.getScore());
    }
  }
}
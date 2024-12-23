package com.sparta.kidscafe.domain.report.service;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.PageResponseDto;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.common.enums.ReportType;
import com.sparta.kidscafe.common.enums.RoleType;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.domain.report.dto.request.ReportRequestDto;
import com.sparta.kidscafe.domain.report.dto.request.ReportUpdateDto;
import com.sparta.kidscafe.domain.report.dto.response.ReportResponseDto;
import com.sparta.kidscafe.domain.report.entity.Report;
import com.sparta.kidscafe.domain.report.repository.ReportRepository;
import com.sparta.kidscafe.domain.review.entity.Review;
import com.sparta.kidscafe.domain.review.repository.ReviewRepository;
import com.sparta.kidscafe.domain.user.entity.User;
import com.sparta.kidscafe.domain.user.repository.UserRepository;
import com.sparta.kidscafe.exception.BusinessException;
import com.sparta.kidscafe.exception.ErrorCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

  @Mock
  private ReportRepository reportRepository;
  @Mock
  private ReviewRepository reviewRepository;
  @Mock
  private CafeRepository cafeRepository;
  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private ReportService reportService;

  @Test
  void createReport_ShouldCreateReportSuccessfully() {
    // Given
    Long userId = 1L;
    Long reviewId = 2L;
    Long cafeId = 3L;
    AuthUser authUser = new AuthUser(userId, "test@example.com", RoleType.OWNER);
    Cafe cafe = Cafe.builder()
        .id(cafeId)
        .name("cafe")
        .build();
    ReportRequestDto request = new ReportRequestDto("신고 내용");

    User user = new User(userId, "testUser", RoleType.OWNER);
    Review review = new Review(reviewId, user, cafe, 4.5, "content");
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
    when(cafeRepository.existsByIdAndUserId(review.getCafe().getId(), userId)).thenReturn(true);

    // When
    StatusDto result = reportService.createReport(authUser, request, reviewId);

    // Then
    assertEquals(HttpStatus.CREATED.value(), result.getStatus());
    assertEquals("신고 등록 완료", result.getMessage());
    verify(reportRepository).save(any(Report.class));
  }

  @Test
  void createReport_ShouldThrowException_WhenUserNotFound() {
    // Given
    Long userId = 1L;
    Long reviewId = 2L;
    AuthUser authUser = new AuthUser(userId, "test@example.com", RoleType.OWNER);
    ReportRequestDto request = new ReportRequestDto("신고 내용");

    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    // When & Then
    BusinessException exception = assertThrows(BusinessException.class,
        () -> reportService.createReport(authUser, request, reviewId));
    assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
  }

  @Test
  void updateReport_ShouldUpdateStatusSuccessfully() {
    // Given
    Long reportId = 1L;
    Report report = new Report();
    report.UpdateReportType(ReportType.PENDING);
    ReportUpdateDto request = new ReportUpdateDto(ReportType.IN_PROGRESS);

    when(reportRepository.findById(reportId)).thenReturn(Optional.of(report));

    // When
    StatusDto result = reportService.updateReport(request, reportId);

    // Then
    assertEquals(HttpStatus.OK.value(), result.getStatus());
    assertEquals("신고 상태변경 완료", result.getMessage());
    assertEquals(ReportType.IN_PROGRESS, report.getStatus());
  }

  @Test
  void updateReport_ShouldThrowException_WhenStatusChangeNotAllowed() {
    // Given
    Long reportId = 1L;
    Report report = new Report();
    report.UpdateReportType(ReportType.COMPLETED);
    ReportUpdateDto request = new ReportUpdateDto(ReportType.REJECTED);

    when(reportRepository.findById(reportId)).thenReturn(Optional.of(report));

    // When & Then
    BusinessException exception = assertThrows(BusinessException.class,
        () -> reportService.updateReport(request, reportId));
    assertEquals(ErrorCode.REPORT_STATUS_NOT_CHANGEABLE, exception.getErrorCode());
  }

  @Test
  void getMyReports_ShouldReturnReportsSuccessfully() {
    // Given
    Long userId = 1L;
    AuthUser authUser = new AuthUser(userId, "test@example.com", RoleType.OWNER);
    Pageable pageable = PageRequest.of(0, 10);

    Report report = new Report(1L, new User(userId, "testUser", RoleType.OWNER), new Review(), "신고 내용", ReportType.PENDING);
    Page<Report> reports = new PageImpl<>(List.of(report));

    when(reportRepository.findAllByUserId(userId, pageable)).thenReturn(reports);

    // When
    PageResponseDto<ReportResponseDto> result = reportService.getMyReports(authUser, pageable);

    // Then
    assertEquals(1, result.getData().size());
    assertEquals("신고 목록 조회 성공", result.getMessage());
  }
}

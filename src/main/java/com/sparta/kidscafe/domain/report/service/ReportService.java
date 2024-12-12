package com.sparta.kidscafe.domain.report.service;

import static com.sparta.kidscafe.exception.ErrorCode.FORBIDDEN;
import static com.sparta.kidscafe.exception.ErrorCode.INVALID_REPORT_STATUS;
import static com.sparta.kidscafe.exception.ErrorCode.REPORT_NOT_FOUND;
import static com.sparta.kidscafe.exception.ErrorCode.REPORT_STATUS_NOT_CHANGEABLE;
import static com.sparta.kidscafe.exception.ErrorCode.REVIEW_NOT_FOUND;
import static com.sparta.kidscafe.exception.ErrorCode.USER_NOT_FOUND;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.PageResponseDto;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.common.enums.ReportType;
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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportService {

  private final ReportRepository reportRepository;
  private final ReviewRepository reviewRepository;
  private final CafeRepository cafeRepository;
  private final UserRepository userRepository;

  public StatusDto createReport(AuthUser authUser, @Valid ReportRequestDto request, Long reviewId) {

    Long id = authUser.getId();

    User user = userRepository.findById(id).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

    Review review = reviewRepository.findById(reviewId).orElseThrow(()-> new BusinessException (REVIEW_NOT_FOUND));

    Boolean ownerCafe = cafeRepository.existsByIdAndUserId(review.getCafe().getId(),id);

    if (!ownerCafe) {
      throw new BusinessException (FORBIDDEN);
    }

    Report newReport = Report.builder()
        .user(user)
        .review(review)
        .content(request.content())
        .build();

    reportRepository.save(newReport);

    return StatusDto.builder()
        .status(HttpStatus.CREATED.value())
        .message("신고 등록 완료")
        .build();
  }

  @Transactional(readOnly = true)
  public PageResponseDto<ReportResponseDto> getMyReports(AuthUser authUser, Pageable pageable) {

    Long id = authUser.getId();

    Page<Report> reports = reportRepository.findAllByUserId(id, pageable);

    Page<ReportResponseDto> reportDtos = reports.map(report -> new ReportResponseDto(
        report.getId(),
        report.getUser().getId(),
        report.getReview().getId(),
        report.getContent(),
        report.getStatus()
    ));

    return PageResponseDto.success(reportDtos,HttpStatus.OK, "신고 목록 조회 성공");
  }

  public PageResponseDto<ReportResponseDto> getReports(Pageable pageable) {

    Page<Report> reports = reportRepository.findAllByOrderByCreatedAtDesc(pageable);

    Page<ReportResponseDto> reportDtos = reports.map(report -> new ReportResponseDto(
        report.getId(),
        report.getUser().getId(),
        report.getReview().getId(),
        report.getContent(),
        report.getStatus()
    ));

    return PageResponseDto.success(reportDtos,HttpStatus.OK, "신고 목록 조회 성공");
  }

  public StatusDto updateReport(ReportUpdateDto request, Long reportId) {

    Report report = reportRepository.findById(reportId).orElseThrow(()-> new BusinessException (REPORT_NOT_FOUND));

    ReportType currentStatus = report.getStatus();
    ReportType requestedStatus = request.status(); // 요청된 상태

    // 상태 변경 로직
    switch (currentStatus) {
      case PENDING:
        // PENDING 상태에서 IN_PROGRESS로 변경
        if (requestedStatus == ReportType.IN_PROGRESS) {
          report.UpdateReportType(ReportType.IN_PROGRESS);
        } else {
          throw new BusinessException(REPORT_STATUS_NOT_CHANGEABLE);
        }
        break;

      case IN_PROGRESS:
        // IN_PROGRESS 상태에서 COMPLETED 또는 REJECTED로 변경
        if (requestedStatus == ReportType.COMPLETED) {
          report.UpdateReportType(ReportType.COMPLETED);
        } else if (requestedStatus == ReportType.REJECTED) {
          report.UpdateReportType(ReportType.REJECTED);
        } else {
          throw new BusinessException(REPORT_STATUS_NOT_CHANGEABLE);
        }
        break;

      case COMPLETED:
        // COMPLETED 상태는 변경 불가
        throw new BusinessException(REPORT_STATUS_NOT_CHANGEABLE);

      case REJECTED:
        // REJECTED 상태는 변경 불가
        throw new BusinessException(REPORT_STATUS_NOT_CHANGEABLE);

      default:
        throw new BusinessException(INVALID_REPORT_STATUS);
    }

    // 상태 변경 후 저장
    reportRepository.save(report);

    return StatusDto.builder()
        .status(HttpStatus.OK.value())
        .message("신고 상태변경 완료")
        .build();
    }
}

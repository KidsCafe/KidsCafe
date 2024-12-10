package com.sparta.kidscafe.domain.report.service;

import static com.sparta.kidscafe.exception.ErrorCode.FORBIDDEN;
import static com.sparta.kidscafe.exception.ErrorCode.REVIEW_NOT_FOUND;
import static com.sparta.kidscafe.exception.ErrorCode.USER_NOT_FOUND;

import com.sparta.kidscafe.common.dto.AuthUser;
import com.sparta.kidscafe.common.dto.PageResponseDto;
import com.sparta.kidscafe.common.dto.StatusDto;
import com.sparta.kidscafe.domain.cafe.repository.CafeRepository;
import com.sparta.kidscafe.domain.report.dto.request.ReportRequestDto;
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


}

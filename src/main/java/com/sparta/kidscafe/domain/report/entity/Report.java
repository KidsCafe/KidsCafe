package com.sparta.kidscafe.domain.report.entity;

import com.sparta.kidscafe.common.entity.Timestamped;
import com.sparta.kidscafe.common.enums.ReportType;
import com.sparta.kidscafe.domain.review.entity.Review;
import com.sparta.kidscafe.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "report")
public class Report extends Timestamped {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "review_id")
  private Review review;

  @Column(nullable = false)
  private String content;

  @Builder.Default
  @Enumerated(value = EnumType.STRING)
  private ReportType status = ReportType.PENDING;

  public void UpdateReportType(ReportType status) {
    this.status = status;
  }
}


package com.sparta.kidscafe.domain.fee.entity;

import com.sparta.kidscafe.common.entity.Timestamped;
import com.sparta.kidscafe.common.enums.AgeGroup;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.fee.dto.request.FeeUpdateRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "fee")
public class Fee extends Timestamped {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cafe_id", nullable = false)
  private Cafe cafe;

  @Enumerated(value = EnumType.STRING)
  private AgeGroup ageGroup;

  @Column(nullable = false, length = 50)
  private int fee;

  public void update(FeeUpdateRequestDto feeUpdateRequestDto) {
    this.ageGroup = feeUpdateRequestDto.getAgeGroup();
    this.fee = feeUpdateRequestDto.getFee();
  }
}

package com.sparta.kidscafe.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 엔티티의 생성/수정 시간을 자동으로 관리하는 추상 클래스입니다.
 *
 * @since 2024-10-03
 */
@Getter
@MappedSuperclass // 추상클래스에 선언한 변수를 컬럼으로 인식한다.
@EntityListeners(AuditingEntityListener.class) // 자동으로 시간을 넣어주는 기능 사용 가능
public abstract class Timestamped {
  @CreatedDate
  @Column(updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column
  @Temporal(TemporalType.TIMESTAMP)
  private LocalDateTime updatedAt;
}

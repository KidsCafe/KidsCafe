package com.sparta.kidscafe.domain.cafe.entity;

import com.sparta.kidscafe.common.entity.Timestamped;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "cafe_image")
public class CafeImage extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cafe_id", nullable = false)
  private Cafe cafe;

  @Column(nullable = false)
  private String imagePath;

  // Cafe 객체와 imagePath를 인자로 받는 생성자 추가
  public CafeImage(Cafe cafe, String imagePath) {
    this.cafe = cafe;
    this.imagePath = imagePath;
  }

  // Cafe 및 imagePath를 업데이트하는 메서드
  public void update(Cafe cafe, String imagePath) {
    this.cafe = cafe;
    this.imagePath = imagePath;
  }

  // 관계 해제 메서드
  public void delete() {
    this.cafe = null;
  }
}

package com.sparta.kidscafe.domain.cafe.entity;

import com.sparta.kidscafe.common.entity.Timestamped;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

  @Column
  private Long cafeId;

  @Column(nullable = false)
  private String imagePath;

  public void update(Long cafeId) {
    this.cafeId = cafeId;
  }

  public void delete() {
    cafeId = null;
  }
}

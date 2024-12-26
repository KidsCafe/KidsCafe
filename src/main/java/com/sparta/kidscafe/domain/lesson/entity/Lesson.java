package com.sparta.kidscafe.domain.lesson.entity;

import com.sparta.kidscafe.common.entity.Timestamped;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.lesson.dto.request.LessonCreateRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lesson")
public class Lesson extends Timestamped {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cafe_id")
  private Cafe cafe;

  @Column(nullable = false, length = 50)
  private String name;

  @Column(nullable = false)
  private String description;

  @Column
  private int price;

  public void update(LessonCreateRequestDto requestDto) {
    this.name = requestDto.getName();
    this.description = requestDto.getDescription();
    this.price = requestDto.getPrice();
  }
}

package com.sparta.kidscafe.domain.lesson.entity;

import com.sparta.kidscafe.common.entity.Timestamped;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.lesson.dto.request.LessonCreateRequestDto;
import jakarta.persistence.*;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
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

  // Map<String, Object> 데이터를 처리하는 생성자 추가
  public Lesson(Cafe cafe, Map<String, Object> data) {
    this.cafe = cafe;
    this.name = data.get("name").toString();
    this.description = data.get("description").toString();
    this.price = Integer.parseInt(data.getOrDefault("price", "0").toString());
  }

  public void update(LessonCreateRequestDto requestDto) {
    this.name = requestDto.getName();
    this.description = requestDto.getDescription();
    this.price = requestDto.getPrice();
  }
}

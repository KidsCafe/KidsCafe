package com.sparta.kidscafe.domain.room.entity;

import com.sparta.kidscafe.common.entity.Timestamped;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Table(name = "room")
public class Room extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cafe_id")
  private Cafe cafe;

  @Column(nullable = false)
  private String name;

  @Column
  private String description;

  @Column(nullable = false)
  private int minCount;

  @Column(nullable = false)
  private int maxCount;

  @Column(nullable = false)
  private int price;

  // Map<String, Object> 데이터를 처리하는 생성자
  public Room(Cafe cafe, Map<String, Object> data) {
    this.cafe = cafe;
    this.name = data.get("name").toString();
    this.description = data.getOrDefault("description", "").toString();
    this.minCount = Integer.parseInt(data.getOrDefault("minCount", "1").toString());
    this.maxCount = Integer.parseInt(data.getOrDefault("maxCount", "2").toString());
    this.price = Integer.parseInt(data.getOrDefault("price", "0").toString());
  }

  public void updateRoom(String name, String description, int minCount, int maxCount, int price) {
    this.name = name;
    this.description = description;
    this.minCount = minCount;
    this.maxCount = maxCount;
    this.price = price;
  }
}
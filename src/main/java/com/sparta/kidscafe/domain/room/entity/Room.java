package com.sparta.kidscafe.domain.room.entity;

import com.sparta.kidscafe.common.entity.Timestamped;
import com.sparta.kidscafe.domain.cafe.entity.Cafe;
import com.sparta.kidscafe.domain.room.dto.request.RoomCreateRequestDto;
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
import org.hibernate.sql.Update;

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

  public void updateRoom(RoomCreateRequestDto requestDto) {
    this.name = requestDto.getName();
    this.description = requestDto.getDescription();
    this.minCount = requestDto.getMinCount();
    this.maxCount = requestDto.getMaxCount();
    this.price = requestDto.getPrice();
  }
}

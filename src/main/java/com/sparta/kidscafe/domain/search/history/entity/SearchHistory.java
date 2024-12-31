package com.sparta.kidscafe.domain.search.history.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;

@Entity
@Getter
@RequiredArgsConstructor
public class SearchHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String region;

  @Column
  private String address;

  @Column
  private Point location;

  @Column
  private Integer size;

  @Column
  private Boolean multiFamily;

  @Column
  private Boolean parking;

  @Column
  private Boolean restaurant;

  @Column
  private Boolean careService;

  @Column
  private Boolean swimmingPool;

  @Column
  private Boolean clothesRental;

  @Column
  private Boolean monitoring;

  @Column
  private Boolean feedingRoom;

  @Column
  private Boolean outdoorPlayground;

  @Column
  private Boolean safetyGuard;

  @Column
  private LocalTime openedAt;

  @Column
  private LocalTime closedAt;

  @Column
  private Long userId;

  @Column
  private String name;

  @Column
  private String ageGroup;

  @Column
  private Integer minPrice;

  @Column
  private Integer maxPrice;

  @Column
  private Double minStar;

  @Column
  private Double maxStar;

  @Column
  private Boolean opening;

  @Column
  private Boolean existRoom;

  @Column
  private Boolean existLesson;

  @Column
  private Boolean adultPrice;
}

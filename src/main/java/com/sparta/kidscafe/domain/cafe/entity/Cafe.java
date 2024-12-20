package com.sparta.kidscafe.domain.cafe.entity;

import com.sparta.kidscafe.common.entity.Timestamped;
import com.sparta.kidscafe.domain.bookmark.entity.Bookmark;
import com.sparta.kidscafe.domain.cafe.dto.request.CafeSimpleRequestDto;
import com.sparta.kidscafe.domain.fee.entity.Fee;
import com.sparta.kidscafe.domain.lesson.entity.Lesson;
import com.sparta.kidscafe.domain.pricepolicy.entity.PricePolicy;
import com.sparta.kidscafe.domain.room.entity.Room;
import com.sparta.kidscafe.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cafe")
public class Cafe extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Column(nullable = false, length = 50)
  private String name;

  @Column(nullable = false)
  private String region;

  @Column(nullable = false)
  private String address;

  @Column
  private Point location;

  @Column(nullable = false)
  private int size;

  @Column(nullable = false)
  private boolean multiFamily;

  @Column
  private String dayOff;

  @Column(nullable = false)
  private boolean parking;

  @Column(nullable = false)
  private boolean restaurant;

  @Column(nullable = false)
  private boolean careService;

  @Column(nullable = false)
  private boolean swimmingPool;

  @Column(nullable = false)
  private boolean clothesRental;

  @Column(nullable = false)
  private boolean monitoring;

  @Column(nullable = false)
  private boolean feedingRoom;

  @Column(nullable = false)
  private boolean outdoorPlayground;

  @Column(nullable = false)
  private boolean safetyGuard;

  @Column
  private String hyperlink;

  @Column(updatable = false)
  private LocalTime openedAt;

  @Column(updatable = false)
  private LocalTime closedAt;

  @Builder.Default
  @OneToMany(
      mappedBy = "cafe",
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
      orphanRemoval = true)
  private List<Bookmark> bookmarks = new ArrayList<>();

  @Builder.Default
  @OneToMany(
      mappedBy = "cafe",
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
      orphanRemoval = true)
  private List<Room> rooms = new ArrayList<>();

  @Builder.Default
  @OneToMany(
      mappedBy = "cafe",
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
      orphanRemoval = true)
  private List<Fee> fees = new ArrayList<>();

  @Builder.Default
  @OneToMany(
      mappedBy = "cafe",
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
      orphanRemoval = true)
  private List<PricePolicy> pricePolicies = new ArrayList<>();

  @Builder.Default
  @OneToMany(
      mappedBy = "cafe",
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
      orphanRemoval = true)
  private List<Lesson> lessons = new ArrayList<>();

  public void update(CafeSimpleRequestDto requestDto, Point location) {
    name = requestDto.getName();
    region = requestDto.getRegion();
    address = requestDto.getAddress();
    size = requestDto.getSize();
    multiFamily = requestDto.isMultiFamily();
    dayOff = requestDto.getDayOff();
    parking = requestDto.isParking();
    hyperlink = requestDto.getHyperLink();
    openedAt = requestDto.getOpenedAt();
    closedAt = requestDto.getClosedAt();
    this.location = location;
  }

  public void updateLocation(Point location) {
    this.location = location;
  }
}
